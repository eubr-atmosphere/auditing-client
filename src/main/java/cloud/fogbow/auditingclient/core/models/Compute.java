package cloud.fogbow.auditingclient.core.models;

import java.util.List;

public class Compute {
    private String instanceId;
    private String serializedSystemUser;
    private String cloudName;
    private List<AssignedIp> assignedIps;

    public Compute(String instanceId, String serializedSystemUser) {
        this.instanceId = instanceId;
        this.serializedSystemUser = serializedSystemUser;
    }

    public Compute(String instanceId, String serializedSystemUser, List<AssignedIp> assignedIps) {
        this.instanceId = instanceId;
        this.serializedSystemUser = serializedSystemUser;
        this.assignedIps = assignedIps;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public List<AssignedIp> getAssignedIps() {
        return assignedIps;
    }

    public void setAssignedIps(List<AssignedIp> assignedIps) {
        this.assignedIps = assignedIps;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }
}
