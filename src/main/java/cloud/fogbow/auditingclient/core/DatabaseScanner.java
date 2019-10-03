package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.FederatedNetwork;
import cloud.fogbow.auditingclient.core.models.Ip;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.BashScriptRunner;
import cloud.fogbow.common.util.GsonHolder;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseScanner {
    private static final String SQL_SPLIT_DIVIDER = "\\|";
    private static final String SQL_INDEX_OF_DIVIDER = "|";
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

    public List<FederatedNetwork> scanActiveFederatedNetworks() throws UnexpectedException{
        String script = Paths.get("").toAbsolutePath().toString() + "/src/main/java/cloud/fogbow/auditingclient/core/scripts/scan-active-fednet.sh";
        String[] scriptExecutorCommand = {script};
        BashScriptRunner.Output output = bashScriptRunner.runtimeRun(scriptExecutorCommand);
        return getFedNetFromOutput(output.getContent());
    }

    private List<Compute> getComputeFromOutput(String content) {
        Compute[] result = GsonHolder.getInstance().fromJson(content, Compute[].class);

        return Arrays.asList(result);
    }

    private List<FederatedNetwork> getFedNetFromOutput(String content) {
        List<FederatedNetwork> result = new ArrayList<>();
        int sqlFieldSepartorIndex;
        while((sqlFieldSepartorIndex=content.indexOf(SQL_INDEX_OF_DIVIDER)) != -1) {
            String serializedSysUser = content.substring(0, sqlFieldSepartorIndex-1).trim();
            content = content.substring(sqlFieldSepartorIndex+1).trim();
            String ips = content.split(SQL_SPLIT_DIVIDER)[0];
            List<Ip> ipInstances = mapStringToIps(ips);
            content = content.substring(ips.length()+2).trim();
            String computeId = content.split(SQL_SPLIT_DIVIDER)[0];
            content = content.substring(computeId.length()+2).trim();
            String orderId = content.split(" ")[0];
            content = content.substring(orderId.length()+1).trim();
            Map<String, List<Ip>> ipAddresses = new HashMap<>();
            ipAddresses.put(computeId, ipInstances);
            FederatedNetwork order = result.stream().filter(ord -> ord.getOrderId().equals(orderId)).collect(Collectors.toList()).iterator().next();
            if(order == null) {
                order = new FederatedNetwork(serializedSysUser, ipAddresses, orderId);
            } else {
                order.getIpAddresses().put(computeId, ipInstances);
            }
            result.remove(order);
            result.add(order);
        }
        return result;
    }

    private List<Ip> mapStringToIps(String ips) {
        List<Ip> result = new ArrayList<>();
        int splitIndex;
        while ((splitIndex=ips.indexOf(' ')) != -1) {
            result.add(new Ip(ips.substring(0, splitIndex).trim()));
            ips = ips.split(" ")[1];
        }
        return result;
    }
}
