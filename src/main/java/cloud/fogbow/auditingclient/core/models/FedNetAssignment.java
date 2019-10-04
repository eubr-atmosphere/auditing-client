package cloud.fogbow.auditingclient.core.models;

public class FedNetAssignment {
    private String serializedSystemUser;
    private String computeId;
    private String id;
    private String ip;

    public FedNetAssignment() {
    }

    public String getSerializedSystemUser() {
        return serializedSystemUser;
    }

    public String getComputeId() {
        return computeId;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }
}
