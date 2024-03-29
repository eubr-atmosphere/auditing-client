package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.AssignedIp;
import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.FedNetAssignment;
import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.BashScriptRunner;
import cloud.fogbow.common.util.GsonHolder;
import org.apache.log4j.Logger;

import java.nio.file.Paths;
import java.util.*;

public class DatabaseScanner {
    private static final Logger LOGGER = Logger.getLogger(DatabaseScanner.class);

    private BashScriptRunner bashScriptRunner;

    public DatabaseScanner() {
        this.bashScriptRunner = new BashScriptRunner();
    }

    public List<Compute> scanActiveComputes() throws UnexpectedException {
        String script = Paths.get("").toAbsolutePath().toString() + "/src/main/java/cloud/fogbow/auditingclient/core/scripts/scan-active-computes.sh";
        String[] scriptExecutorCommand = {script};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        return getComputeFromOutput(output.getContent());
    }

    public List<Compute> scanActiveFederatedNetworks() throws UnexpectedException{
        String script = Paths.get("").toAbsolutePath().toString() + "/src/main/java/cloud/fogbow/auditingclient/core/scripts/scan-active-fednet.sh";
        String[] scriptExecutorCommand = {script};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        return getFedNetFromOutput(output.getContent());
    }

    private List<Compute> getComputeFromOutput(String content) {
        LOGGER.info(content);
        if(content != null && !content.trim().isEmpty()) {
            content = content.trim();
            Compute[] result = GsonHolder.getInstance().fromJson(content, Compute[].class);
            return Arrays.asList(result);
        }
        return new ArrayList<>();
    }

    private List<Compute> getFedNetFromOutput(String content) throws UnexpectedException {
        LOGGER.info(content);
        if(content != null && !content.trim().isEmpty()) {
            content = content.trim();
            FedNetAssignment[] fedNetAssignments = GsonHolder.getInstance().fromJson(content, FedNetAssignment[].class);
            return getComputesFromfednet(fedNetAssignments);
        }
        return new ArrayList<>();
    }

    private List<Compute> getComputesFromfednet(FedNetAssignment[] fedNetAssignments)   throws UnexpectedException {
        if(fedNetAssignments == null) {
            return new ArrayList<>();
        }

        Map<String, Compute> computes = new HashMap<>();
        for (FedNetAssignment assignment : fedNetAssignments) {
            String orderId = assignment.getComputeId();
            if (!computes.containsKey(orderId)) {
                Compute compute = getComputeFromOrderId(orderId);
                compute.setAssignedIps(new ArrayList<>());
                computes.put(orderId, compute);
            }
            Compute compute = computes.get(orderId);
            AssignedIp assignedIp = new AssignedIp(assignment.getIp(), assignment.getId(), compute.getInstanceId(), AssignedIp.Type.FEDNET);
            compute.getAssignedIps().add(assignedIp);
        }
        return new ArrayList(computes.values());
    }

    private Compute getComputeFromOrderId(String orderId) throws UnexpectedException {
        String script = Paths.get("").toAbsolutePath().toString() + "/src/main/java/cloud/fogbow/auditingclient/core/scripts/get-compute-instance-id.sh";
        String[] scriptExecutorCommand = {script, orderId};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        return GsonHolder.getInstance().fromJson(output.getContent(), Compute.class);
    }
}
