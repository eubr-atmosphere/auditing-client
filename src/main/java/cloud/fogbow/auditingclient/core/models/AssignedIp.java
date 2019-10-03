package cloud.fogbow.auditingclient.core.models;

import com.google.gson.GsonBuilder;

public class AssignedIp {
    private String ip;
    private String networkId;
    private Type type;

    public AssignedIp(String ip, String networkId, Type type) {
        this.ip = ip;
        this.networkId = networkId;
        this.type = type;
    }

    public enum Type {
        CLOUD("cloud"), FEDNET("fednet");

        private String type;

        Type(String type) {
            this.type = type;
        }
    }
}
