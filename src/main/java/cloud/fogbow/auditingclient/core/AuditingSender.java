package cloud.fogbow.auditingclient.core;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import cloud.fogbow.auditingclient.core.models.AuditingMessage;
import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import com.google.gson.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AuditingSender {
    private String endpoint;
    private Properties properties;
    private static final String APPLICATION_JSON_HEADER_VALUE = "application/json";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private static final String SIGNATURE_HEADER_KEY = "Signature";

    public AuditingSender() {
        properties = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY);
        this.endpoint = properties.getProperty(Constants.SERVER_ENDPOINT_KEY);
    }

    public void send(AuditingMessage message) throws FogbowException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        message.setCurrentTimestamp(currentTimestamp);
        message.setFogbowSite(properties.getProperty(Constants.SITE_KEY));
        message.setClientId(properties.getProperty(Constants.CLIENT_ID_KEY));
        String body = jsonfy(message);

        try {
            HttpRequestClient.doGenericRequest(HttpMethod.POST, endpoint, getHeaders(message), body);
        } catch (Exception e) {
            throw new FogbowException(e.getMessage());
        }
    }

    private String jsonfy(AuditingMessage message) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Date.class, new DateSerializer());
        return gson
                .create()
                .toJson(message);
    }

    class DateSerializer implements JsonSerializer {
        @Override
        public JsonElement serialize(Object date, Type typeOfSrc, JsonSerializationContext context) {
            return date == null ? null : new JsonPrimitive(((Date) date).getTime());
        }
    }

    private Map<String, String> getHeaders(AuditingMessage message) throws IOException, GeneralSecurityException {
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_HEADER_VALUE);

        RSAPrivateKey privateKey = CryptoUtil.getPrivateKey(Paths.get("").toAbsolutePath().toString() + "/src/main/resources/private/private.key");
        String signature = CryptoUtil.sign(privateKey, message.toString());
        headers.put(SIGNATURE_HEADER_KEY, signature);

        return headers;
    }
}
