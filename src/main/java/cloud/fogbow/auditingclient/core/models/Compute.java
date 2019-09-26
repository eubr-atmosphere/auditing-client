package cloud.fogbow.auditingclient.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Compute {
    private String instanceId;
    private Map<String, List<Ip>> ipAddresses = new HashMap<>();
    private String serializedSystemUser;

    public Compute(String instanceId, String serializedSystemUser) {
        this.serializedSystemUser = serializedSystemUser;
        this.instanceId = instanceId;
    }

    public Compute(String instanceId, Map<String, List<Ip>> ipAddresses, String serializedSystemUser) {
        this.instanceId = instanceId;
        this.ipAddresses = ipAddresses;
        this.serializedSystemUser = serializedSystemUser;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Map<String, List<Ip>> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(Map<String, List<Ip>> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public String getSerializedSystemUser() {
        return serializedSystemUser;
    }

    public void setSerializedSystemUser(String serializedSystemUser) {
        this.serializedSystemUser = serializedSystemUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compute compute = (Compute) o;
        return Objects.equals(instanceId, compute.instanceId) &&
                Objects.equals(ipAddresses, compute.ipAddresses) &&
                Objects.equals(serializedSystemUser, compute.serializedSystemUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, ipAddresses, serializedSystemUser);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setAddresses(Map<String, List<Ip>> addresses) {
        this.ipAddresses = addresses;
    }
}
