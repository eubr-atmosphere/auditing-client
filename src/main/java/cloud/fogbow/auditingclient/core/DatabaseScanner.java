package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.FederatedNetwork;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.BashScriptRunner;

import java.util.List;

public class DatabaseScanner {
    private BashScriptRunner bashScriptRunner;

    public DatabaseScanner() {
        this.bashScriptRunner = new BashScriptRunner();
    }

    public List<Compute> scanActiveComputes() throws UnexpectedException {
        String[] scriptExecutorCommand = {"cd scripts", "&&", "./scan-active-computes.sh"};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        System.out.println(output.getContent());
        return null;
    }

    public List<FederatedNetwork> scanActiveFederatedNetworks() throws UnexpectedException{
        String[] scriptExecutorCommand = {"cd scripts", "&&", "./scan-active-fednet.sh"};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        System.out.println(output.getContent());
        return null;
    }
}
