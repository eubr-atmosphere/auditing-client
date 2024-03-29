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
                LOGGER.info("Reading active computes");
                List<Compute> activeComputes = dbScanner.scanActiveComputes();

                LOGGER.info("Reading active fednets");
                List<Compute> activeFedNets = dbScanner.scanActiveFederatedNetworks();

                try {
                    if(!activeComputes.isEmpty()) {
                        OpenStackCloudUtil.getInstance().assignComputesIps(activeComputes);
                    }
                } catch (Exception ex) {
                    LOGGER.debug(ex.getMessage());
                }

                collapseSameInstanceIds(activeComputes, activeFedNets);

                AuditingMessage message = new AuditingMessage();
                message.addComputes(activeComputes);
                message.addComputes(activeFedNets);

                LOGGER.info("Sending computes...");
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
}
