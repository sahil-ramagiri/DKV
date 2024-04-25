package sk.dkv;

import io.microraft.RaftEndpoint;
import io.microraft.RaftNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationEventsHandler {

    @Autowired
    RaftNode raftNode;

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void startRaftNode(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Starting Raft Node");
        raftNode.start().join();
    }
}
