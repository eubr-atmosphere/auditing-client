package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.FederatedNetwork;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.BashScriptRunner;

import java.nio.file.Paths;
import java.util.List;

public class DatabaseScanner {
    private BashScriptRunner bashScriptRunner;

    public DatabaseScanner() {
        this.bashScriptRunner = new BashScriptRunner();
    }

    public List<Compute> scanActiveComputes() throws UnexpectedException {
        String script = Paths.get("").toAbsolutePath().toString() + "/src/main/java/cloud/fogbow/auditingclient/core/scripts/scan-active-computes.sh";
        String[] scriptExecutorCommand = {script};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        System.out.println(output.getContent());
        return null;
    }

    public List<FederatedNetwork> scanActiveFederatedNetworks() throws UnexpectedException{
        String script = Paths.get("").toAbsolutePath().toString() + "/src/main/java/cloud/fogbow/auditingclient/core/scripts/scan-active-fednet.sh";
        String[] scriptExecutorCommand = {script};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        System.out.println(output.getContent());
        return null;
    }
}
