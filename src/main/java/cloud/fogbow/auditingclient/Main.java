package cloud.fogbow.auditingclient;

import cloud.fogbow.auditingclient.core.processors.SyncProcessor;

public class Main {

    public void startProcessor() {
        Thread processor = new Thread(new SyncProcessor());
        processor.start();
    }
}
