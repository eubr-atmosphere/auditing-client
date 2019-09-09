package cloud.fogbow.auditingclient.core;

import cloud.fogbow.auditingclient.core.models.AuditingMessage;

import java.sql.Timestamp;

public class AuditingSender {

    public AuditingSender() {

    }

    public void send(AuditingMessage message) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        message.setCurrentTimestamp(currentTimestamp);
    }
}
