package cloud.fogbow.auditingclient.core.models;

import java.sql.Timestamp;
import java.util.List;

public class AuditingMessage {
    private List<Compute> activeComputes;
    private List<FederatedNetwork> activeFederatedNetworks;
    private Timestamp currentTimestamp;
    private String fogbowSite;

    public AuditingMessage(List<Compute> activeComputes, List<FederatedNetwork> federatedNetworks) {
        this.activeComputes = activeComputes;
        this.activeFederatedNetworks = federatedNetworks;
    }

    public void setActiveComputes(List<Compute> activeComputes) {
        this.activeComputes = activeComputes;
    }

    public void setActiveFederatedNetworks(List<FederatedNetwork> activeFederatedNetworks) {
        this.activeFederatedNetworks = activeFederatedNetworks;
    }

    public String getFogbowSite() {
        return fogbowSite;
    }

    public void setFogbowSite(String fogbowSite) {
        this.fogbowSite = fogbowSite;
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
