package sk.dkv.transport;

import io.microraft.RaftNode;
import io.microraft.exception.NotLeaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sk.dkv.KVOperation;

@RestController
@Slf4j
public class MessageReceiver {

    private final RaftNode raftNode;

    public MessageReceiver(RaftNode raftNode) {
        this.raftNode = raftNode;
    }

    @PostMapping("/message")
    public ResponseEntity<Void> handleMessage(@RequestBody ReplicaMessage message) {
        log.debug("Incoming Message: {}", message);
        try {
            raftNode.handle(message.raftMessage);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Encountered error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/ping")
    public ResponseEntity<Void> handleMessage(@RequestBody String node) {
        log.debug("Incoming ping from: {}", node);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kv")
    public ResponseEntity<String> getRequest(@RequestBody KVRequest kvRequest) {
        log.info("Incoming Request: {}", kvRequest);
        try {
            KVOperation operation = switch (kvRequest.operation()) {
                case GET -> new KVOperation.GetOperation(kvRequest.key());
                case SET -> new KVOperation.SetOperation(kvRequest.key(), kvRequest.value());
                case CLEAR -> new KVOperation.ClearOperation(kvRequest.key());
            };
            String result = (String) raftNode.replicate(operation).get().getResult();
            return ResponseEntity.ok(result);
        } catch (NotLeaderException e) {
            return ResponseEntity.status(300).build();
        } catch (Exception e) {
            log.error("Encountered error", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
