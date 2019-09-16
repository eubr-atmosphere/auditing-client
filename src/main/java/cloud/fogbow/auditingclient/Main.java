package cloud.fogbow.auditingclient;

import cloud.fogbow.auditingclient.core.processors.SyncProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Main {

    @PostConstruct
    public void startProcessor() throws Exception{
        Thread processor = new Thread(new SyncProcessor());
        processor.start();
    }
}
