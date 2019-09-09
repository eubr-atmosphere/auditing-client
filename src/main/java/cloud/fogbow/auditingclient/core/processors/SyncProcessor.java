package cloud.fogbow.auditingclient.core.processors;

import cloud.fogbow.auditingclient.core.AuditingSender;
import cloud.fogbow.auditingclient.core.DatabaseScanner;
import cloud.fogbow.auditingclient.core.models.AuditingMessage;
import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.FederatedNetwork;
import org.apache.log4j.Logger;

import java.util.List;

public class SyncProcessor implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(SyncProcessor.class);

    private DatabaseScanner dbScanner;
    private AuditingSender sender;
    private long sleepTime;

    public SyncProcessor() {
        dbScanner = new DatabaseScanner();
        sender = new AuditingSender();
        this.sleepTime = 0L;
    }

    public void run() {
        while(true) {
            try {
                List<Compute> activeComputes = dbScanner.scanActiveComputes();
                List<FederatedNetwork> activeFedNets = dbScanner.scanActiveFederatedNetworks();

                AuditingMessage message = new AuditingMessage(activeComputes, activeFedNets);
                sender.send(message);

                Thread.sleep(this.sleepTime);
            } catch(Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                break;
            }
        }
    }
}
