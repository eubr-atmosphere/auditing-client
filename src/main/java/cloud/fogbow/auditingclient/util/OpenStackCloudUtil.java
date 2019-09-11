package cloud.fogbow.auditingclient.util;

import cloud.fogbow.auditingclient.core.models.Compute;
import cloud.fogbow.auditingclient.core.models.GetComputeResponse;
import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.OpenStackV3User;
import cloud.fogbow.common.plugins.cloudidp.openstack.v3.OpenStackIdentityProviderPlugin;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.connectivity.cloud.openstack.OpenStackHttpClient;
import cloud.fogbow.common.util.connectivity.cloud.openstack.OpenStackHttpToFogbowExceptionMapper;
import org.apache.http.client.HttpResponseException;

import java.util.*;

public class OpenStackCloudUtil {

    private static OpenStackCloudUtil instance;
    private OpenStackV3User cloudUser;
    private String endpoint;
    private OpenStackHttpClient client;

    private OpenStackCloudUtil() throws FogbowException {
        client = new OpenStackHttpClient();

        Properties properties = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY);
        String idpUrl = properties.getProperty(Constants.IDENTITY_PROVIDER_URL_KEY);
        OpenStackIdentityProviderPlugin identityProviderPlugin = new OpenStackIdentityProviderPlugin(idpUrl);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("projectname", properties.getProperty(Constants.PROJECT_NAME_KEY));
        credentials.put("password", properties.getProperty(Constants.PASSWORD_KEY));
        credentials.put("domain", properties.getProperty(Constants.DOMAIN_KEY));
        credentials.put("username", properties.getProperty(Constants.USERNAME_KEY));
        cloudUser = identityProviderPlugin.getCloudUser(credentials);

        endpoint = properties.getProperty(Constants.CLOUD_URL_KEY) + cloudUser.getProjectId() + "/SERVERS";

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
            String jsonResponse = null;
            try {
                jsonResponse = this.client.doGetRequest(endpoint + "/" + compute.getInstanceId(), cloudUser);
                GetComputeResponse computeResponse = GetComputeResponse.fromJson(jsonResponse);
                compute.setAddresses(getAddresses(computeResponse.getAddresses()));
            } catch (HttpResponseException e) {
                OpenStackHttpToFogbowExceptionMapper.map(e);
            }
        }
    }

    private Map<String, String[]> getAddresses(Map<String, GetComputeResponse.Address[]> responseAddresses) {
        Map<String, String[]> result = new HashMap<>();
        for(String key : responseAddresses.keySet()) {
            List<String> addresses = new ArrayList<>();
            for(GetComputeResponse.Address address: responseAddresses.get(key)) {
                addresses.add(address.getAddress());
            }
            result.put(key, (String[])addresses.toArray());
        }
        return result;
    }
}
