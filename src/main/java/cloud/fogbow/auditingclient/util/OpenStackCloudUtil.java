package cloud.fogbow.auditingclient.util;

import cloud.fogbow.auditingclient.core.models.AssignedIp;
import cloud.fogbow.auditingclient.core.models.CloudSettings;
import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.responses.GetComputeResponse;
import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.connectivity.cloud.openstack.OpenStackHttpClient;
import cloud.fogbow.common.util.connectivity.cloud.openstack.OpenStackHttpToFogbowExceptionMapper;
import org.apache.http.client.HttpResponseException;

import java.util.*;

public class OpenStackCloudUtil {

    private static OpenStackCloudUtil instance;
    private OpenStackHttpClient client;
    private Map<String, CloudSettings> cloudSettings;

    private OpenStackCloudUtil() throws FogbowException {
        client = new OpenStackHttpClient();
        cloudSettings = new HashMap<>();

        List<String> cloudNames = getCloudNames();

        for (String cloudName : cloudNames) {
            CloudSettings settings = new CloudSettings(cloudName);
            cloudSettings.put(cloudName, settings);
        }
    }

    public static OpenStackCloudUtil getInstance() throws FogbowException{
        synchronized(OpenStackCloudUtil.class) {
            if(instance == null) {
                return new OpenStackCloudUtil();
            }
        }
        return instance;
    }

    public void assignComputesIps(List<Compute> computes) throws FogbowException {
        for(Compute compute: computes) {
            if (cloudSettings.containsKey(compute.getCloudName())) {
                CloudSettings settings = cloudSettings.get(compute.getCloudName());
                String jsonResponse = null;
                try {
                    jsonResponse = this.client.doGetRequest(settings.getCloudEndpoint() + "/" + compute.getInstanceId(),
                            settings.getCloudUser());
                    GetComputeResponse computeResponse = GetComputeResponse.fromJson(jsonResponse);
                    compute.setAssignedIps(getAddresses(computeResponse.getAddresses(), compute.getInstanceId()));
                } catch (HttpResponseException e) {
                    OpenStackHttpToFogbowExceptionMapper.map(e);
                }
            }
        }
    }

    private List<AssignedIp> getAddresses(Map<String, GetComputeResponse.Address[]> responseAddresses, String computeId) {
        List<AssignedIp> result = new ArrayList<>();
        for(String networkId : responseAddresses.keySet()) {
            for(GetComputeResponse.Address address: responseAddresses.get(networkId)) {
                AssignedIp assignedIp = new AssignedIp(address.getAddress(), networkId, computeId, AssignedIp.Type.CLOUD);
                result.add(assignedIp);
            }
        }
        return result;
    }

    private List<String> getCloudNames() {
        String cloudNames = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY)
                .getProperty(Constants.CLOUD_NAMES_KEY);

        return Arrays.asList(cloudNames.split(","));
    }
}
