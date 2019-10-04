package cloud.fogbow.auditingclient.core.models;

public class AssignedIp {
    private String address;
    private String networkId;
    private String computeId;
    private Type type;

    public AssignedIp(String address, String networkId, String computeId, Type type) {
        this.address = address;
        this.networkId = networkId;
        this.computeId = computeId;
        this.type = type;
    }

    public enum Type {
        CLOUD("cloud"), FEDNET("fednet");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        return this.address += this.computeId += this.networkId += this.type.getValue();
    }
}
