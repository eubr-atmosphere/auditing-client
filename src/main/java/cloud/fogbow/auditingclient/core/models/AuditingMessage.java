package cloud.fogbow.auditingclient.core.models;

import java.sql.Timestamp;
import java.util.List;

public class AuditingMessage {
    private List<Compute> activeComputes;
    private List<FederatedNetwork> activeFederatedNetworks;
    private Timestamp currentTimestamp;

    public AuditingMessage(List<Compute> activeComputes, List<FederatedNetwork> federatedNetworks) {
        this.activeComputes = activeComputes;
        this.activeFederatedNetworks = federatedNetworks;
    }

    public void setCurrentTimestamp(Timestamp currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public List<Compute> getActiveComputes() {
        return activeComputes;
    }

    public List<FederatedNetwork> getActiveFederatedNetworks() {
        return activeFederatedNetworks;
    }

    public Timestamp getCurrentTimestamp() {
        return currentTimestamp;
    }
}
