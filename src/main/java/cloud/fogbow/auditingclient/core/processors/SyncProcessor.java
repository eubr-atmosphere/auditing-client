package cloud.fogbow.auditingclient.core.processors;

import cloud.fogbow.auditingclient.core.AuditingSender;
import cloud.fogbow.auditingclient.core.DatabaseScanner;
import cloud.fogbow.auditingclient.core.models.AuditingMessage;
import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.util.OpenStackCloudUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SyncProcessor implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(SyncProcessor.class);

    private DatabaseScanner dbScanner;
    private AuditingSender sender;
    private long sleepTime;

    public SyncProcessor() {
        dbScanner = new DatabaseScanner();
        sender = new AuditingSender();
        this.sleepTime = 60000;
    }

    @Override
    public void run() {
        while(true) {
            try {
                List<Compute> activeComputes = dbScanner.scanActiveComputes();
                List<Compute> activeFedNets = dbScanner.scanActiveFederatedNetworks();

                OpenStackCloudUtil.getInstance().assignComputesIps(activeComputes);
                collapseSameInstanceIds(activeComputes, activeFedNets);

                logComputes(activeComputes);
                logComputes(activeFedNets);

                AuditingMessage message = new AuditingMessage();
                message.addComputes(activeComputes);
                message.addComputes(activeFedNets);
                sender.send(message);

                Thread.sleep(this.sleepTime);
            } catch(Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }

    private void collapseSameInstanceIds(List<Compute> activeComputes, List<Compute> activeFedNets) {
        List<Compute> computesToRemove = new ArrayList<>();
        for (Compute fedCompute : activeFedNets) {
            Compute foundCompute = activeComputes.stream()
                    .filter(c -> c.getInstanceId() != null && c.getInstanceId().equals(fedCompute.getInstanceId()))
                    .findAny()
                    .orElse(null);
            if (foundCompute != null) {
                foundCompute.getAssignedIps().addAll(fedCompute.getAssignedIps());
                computesToRemove.add(fedCompute);
            }
        }
        activeFedNets.removeAll(computesToRemove);
    }

    private void logComputes(List<Compute> computes) {
        for(Compute compute: computes) {
            LOGGER.info(compute.toString());
        }
    }
}
