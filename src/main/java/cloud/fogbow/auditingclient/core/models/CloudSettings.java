package cloud.fogbow.auditingclient.core.models;

import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.OpenStackV3User;
import cloud.fogbow.common.plugins.cloudidp.openstack.v3.OpenStackIdentityProviderPlugin;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CloudSettings {
    private OpenStackV3User cloudUser;
    private String cloudEndpoint;

    public CloudSettings(String cloudName) throws FogbowException {
        Properties properties = PropertiesUtil.readProperties(HomeDir.getPath() + String.format("%s.conf", cloudName));
        String idpUrl = properties.getProperty(Constants.IDENTITY_PROVIDER_URL_KEY);
        OpenStackIdentityProviderPlugin identityProviderPlugin = new OpenStackIdentityProviderPlugin(idpUrl);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("projectname", properties.getProperty(Constants.PROJECT_NAME_KEY));
        credentials.put("password", properties.getProperty(Constants.PASSWORD_KEY));
        credentials.put("domain", properties.getProperty(Constants.DOMAIN_KEY));
        credentials.put("username", properties.getProperty(Constants.USERNAME_KEY));

        cloudUser = identityProviderPlugin.getCloudUser(credentials);

        cloudEndpoint = properties.getProperty(Constants.CLOUD_URL_KEY) + cloudUser.getProjectId() + "/servers";
    }

    public OpenStackV3User getCloudUser() {
        return cloudUser;
    }

    public String getCloudEndpoint() {
        return cloudEndpoint;
    }
}
