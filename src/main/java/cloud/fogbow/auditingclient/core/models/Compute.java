package cloud.fogbow.auditingclient.core.models;

import java.util.List;

public class Compute {
    private String instanceId;
    private String serializedSystemUser;
    private List<AssignedIp> assignedIps;

    public Compute(String instanceId, String serializedSystemUser, List<AssignedIp> assignedIps) {
        this.instanceId = instanceId;
        this.serializedSystemUser = serializedSystemUser;
        this.assignedIps = assignedIps;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setAssignedIps(List<AssignedIp> assignedIps) {
        this.assignedIps = assignedIps;
    }
}
