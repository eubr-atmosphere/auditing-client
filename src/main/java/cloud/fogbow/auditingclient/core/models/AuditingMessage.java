package cloud.fogbow.auditingclient.core.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuditingMessage {

    private List<Compute> activeComputes;
    private Timestamp currentTimestamp;
    private String fogbowSite;
    private String clientId;

    public AuditingMessage() {
        this.activeComputes = new ArrayList<>();
    }

    public void setFogbowSite(String fogbowSite) {
        this.fogbowSite = fogbowSite;
    }

    public void setCurrentTimestamp(Timestamp currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void addComputes(List<Compute> activeComputes) {
        this.activeComputes.addAll(activeComputes);
    }

    @Override
    public String toString() {
        String value = "";
        value += this.clientId + this.currentTimestamp.getTime() + this.fogbowSite;
        for(Compute compute : this.activeComputes) {
            value += compute.toString();
        }
        return value;
    }
}
