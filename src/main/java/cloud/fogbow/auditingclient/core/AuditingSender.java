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
import java.util.Map;
import java.util.Properties;

public class AuditingSender {
    private String endpoint;

    public AuditingSender() {
        Properties properties = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY);
        this.endpoint = properties.getProperty(Constants.SERVER_ENDPOINT_KEY);
    }

    public void send(AuditingMessage message) throws FogbowException{
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        message.setCurrentTimestamp(currentTimestamp);
        Map<String, String> body = new HashMap<>();
        body.put("message", GsonHolder.getInstance().toJson(message, AuditingMessage.class));

        HttpRequestClient.doGenericRequest(HttpMethod.POST, endpoint, null, body);
    }
}
