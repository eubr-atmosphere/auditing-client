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
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private static final String APPLICATION_JSON_HEADER_VALUE = "application/json";

    public AuditingSender() {
        properties = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY);
        this.endpoint = properties.getProperty(Constants.SERVER_ENDPOINT_KEY);
    }

    public void send(AuditingMessage message) throws FogbowException{
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        message.setCurrentTimestamp(currentTimestamp);
        message.setFogbowSite(properties.getProperty(Constants.SITE_KEY));
        String body = jsonfy(message);

        HttpRequestClient.doGenericRequest(HttpMethod.POST, endpoint, getHeaders(), body);
    }

    private String jsonfy(AuditingMessage message) {
        Map<String, Object> json = new HashMap<>();
        json.put("activeComputes", message.getActiveComputes());
        json.put("activeFederatedNetworks", message.getActiveFederatedNetworks());
        json.put("currentTimestamp", message.getCurrentTimestamp());
        return GsonHolder.getInstance().toJson(json);
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_HEADER_VALUE);
        return headers;
    }
}
