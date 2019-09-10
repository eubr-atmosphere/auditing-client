package cloud.fogbow.auditingclient.util;

import cloud.fogbow.auditingclient.core.models.Compute;

import java.util.List;

public class CloudUtil {

    private String endpoint;
    private static CloudUtil instance;

    private CloudUtil() {
        //here i have to setup the cloudUser and the endpoint that come from a conf file
    }

    public static CloudUtil getInstance() {
        synchronized(CloudUtil.class) {
            if(instance == null) {
                return new CloudUtil();
            }
        }
        return instance;
    }

    public void assignComputesIps(List<Compute> computes) {

    }
}
