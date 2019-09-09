package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.FederatedNetwork;
import cloud.fogbow.common.util.BashScriptRunner;

import java.util.List;

public class DatabaseScanner {
    private BashScriptRunner bashScriptRunner;

    public DatabaseScanner() {
        this.bashScriptRunner = new BashScriptRunner();
    }

    public List<Compute> scanActiveComputes() {
        return null;
    }

    public List<FederatedNetwork> scanActiveFederatedNetworks() {
        return null;
    }
}
