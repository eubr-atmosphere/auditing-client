package cloud.fogbow.auditingclient.core.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuditingMessage {

    private List<Compute> activeComputes;
    private Timestamp currentTimestamp;
    private String fogbowSite;

    public AuditingMessage() {
        this.activeComputes = new ArrayList<>();
    }

    public void setFogbowSite(String fogbowSite) {
        this.fogbowSite = fogbowSite;
    }

    public void setCurrentTimestamp(Timestamp currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public void addComputes(List<Compute> activeComputes) {
        this.activeComputes.addAll(activeComputes);
    }
}
