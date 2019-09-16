package cloud.fogbow.auditingclient.core.models;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FederatedNetwork {
    private String serializedSystemUser;
    private Map<String, List<Ip>> ipAddresses;
    private String orderId;

    public FederatedNetwork(String serializedSystemUser, Map<String, List<Ip>> ipAddresses, String orderId) {
        this.serializedSystemUser = serializedSystemUser;
        this.ipAddresses = ipAddresses;
        this.orderId = orderId;
    }

    public String getSerializedSystemUser() {
        return serializedSystemUser;
    }

    public void setSerializedSystemUser(String serializedSystemUser) {
        this.serializedSystemUser = serializedSystemUser;
    }

    public Map<String, List<Ip>> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(Map<String, List<Ip>> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FederatedNetwork that = (FederatedNetwork) o;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
