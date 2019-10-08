package cloud.fogbow.auditingclient.core;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import cloud.fogbow.auditingclient.core.models.AuditingMessage;
import cloud.fogbow.auditingclient.core.responses.PublicKeyResponse;
import cloud.fogbow.auditingclient.util.constants.Constants;
import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.GsonHolder;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import com.google.gson.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AuditingSender {
    private String serverEndpoint;
    private Properties properties;
    private static final String AUDITING_ENDPOINT = "/auditing";
    private static final String APPLICATION_JSON_HEADER_VALUE = "application/json";
    private static final String CLIENT_ID_HEADER_KEY = "X-Client-Id";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private static final String PUBLIC_KEY_ENDPOINT = "/publicKey";
    private static final String SIGNATURE_HEADER_KEY = "Signature";
    private static final String SYMMETRIC_KEY_HEADER_KEY = "X-Symmetric-Key";

    public AuditingSender() {
        properties = PropertiesUtil.readProperties(HomeDir.getPath() + Constants.CONF_FILE_KEY);
        this.serverEndpoint = properties.getProperty(Constants.SERVER_ENDPOINT_KEY);
    }

    public void send(AuditingMessage message) throws FogbowException {
        if (!message.getActiveComputes().isEmpty()) {
            addInfoToMessage(message);
            Map<String, String> headers = null;
            String endpoint = serverEndpoint + AUDITING_ENDPOINT;
            try {
                String body = jsonfy(message);
                headers = getHeaders(body, message.getClientId());
                RSAPublicKey serverPublicKey = getServerPublicKey();
                String symmetricKey = CryptoUtil.generateAESKey();
                body = CryptoUtil.encryptAES(symmetricKey.getBytes(), body);
                addSymmetricKeyToHeader(headers, symmetricKey, serverPublicKey);
                Map<String, String> bodyAsMap = new HashMap<>();
                bodyAsMap.put("message", body);
                HttpRequestClient.doGenericRequest(HttpMethod.POST, endpoint, headers, bodyAsMap);
            } catch (Exception e) {
                throw new FogbowException(e.getMessage());
            }
        }
    }

    private void addSymmetricKeyToHeader(Map<String, String> headers, String symmetricKey, RSAPublicKey serverPublicKey) throws IOException, GeneralSecurityException {
        String encryptedSymmetricKey = CryptoUtil.encrypt(symmetricKey, serverPublicKey);
        headers.put(SYMMETRIC_KEY_HEADER_KEY, encryptedSymmetricKey);
    }

    private RSAPublicKey getServerPublicKey() throws FogbowException, GeneralSecurityException {
        String endpoint = serverEndpoint + PUBLIC_KEY_ENDPOINT;
        String jsonResponse = HttpRequestClient.doGenericRequest(HttpMethod.GET, endpoint, new HashMap<>(), new HashMap<>())
                .getContent();

        String publicKeyStr = GsonHolder.getInstance().fromJson(jsonResponse, PublicKeyResponse.class).getPublicKey();
        return CryptoUtil.getPublicKeyFromString(publicKeyStr);
    }

    private void addInfoToMessage(AuditingMessage message) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        message.setCurrentTimestamp(currentTimestamp);
        message.setFogbowSite(properties.getProperty(Constants.SITE_KEY));
        message.setClientId(properties.getProperty(Constants.CLIENT_ID_KEY));
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

    private Map<String, String> getHeaders(String body, String clientId) throws IOException, GeneralSecurityException {
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_HEADER_VALUE);

        String signature = getSignature(body);
        headers.put(SIGNATURE_HEADER_KEY, signature);

        headers.put(CLIENT_ID_HEADER_KEY, clientId);

        return headers;
    }

    private String getSignature(String body) throws IOException, GeneralSecurityException {
        RSAPrivateKey privateKey = CryptoUtil.getPrivateKey(Paths.get("").toAbsolutePath().toString() + "/src/main/resources/private/private.key");
        return CryptoUtil.sign(privateKey, body);
    }
}