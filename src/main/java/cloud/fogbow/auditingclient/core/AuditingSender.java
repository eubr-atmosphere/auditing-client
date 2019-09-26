package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.AuditingMessage;
import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.GsonHolder;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AuditingSender {
    private String endpoint;
    private Properties properties;

    public AuditingSender() {
        properties = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY);
        this.endpoint = properties.getProperty(Constants.SERVER_ENDPOINT_KEY);
    }

    public void send(AuditingMessage message) throws FogbowException{
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        message.setCurrentTimestamp(currentTimestamp);
        message.setFogbowSite(properties.getProperty(Constants.SITE_KEY));
        Map<String, String> body = jsonfy(message);

        HttpRequestClient.doGenericRequest(HttpMethod.POST, endpoint, new HashMap<>(), body);
    }

    private Map<String, String> jsonfy(AuditingMessage message) {
        Map<String, String> json = new HashMap<>();
        json.put("activeComputes", GsonHolder.getInstance().toJson(message.getActiveComputes(), List.class));
        json.put("activeFederatedNetworks", GsonHolder.getInstance().toJson(message.getActiveFederatedNetworks(), List.class));
        json.put("currentTimestamp", GsonHolder.getInstance().toJson(message.getCurrentTimestamp(), Timestamp.class));
        return json;
    }
}
