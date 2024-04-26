package sk.dkv.transport;

import io.microraft.RaftEndpoint;
import io.microraft.model.message.RaftMessage;
import io.microraft.transport.Transport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.dkv.ReplicaId;

@Component
@Slf4j
public class MessageBus implements Transport {

    private final RestTemplate restTemplate;

    private final RaftEndpoint localEndpoint;

    public MessageBus(RestTemplate restTemplate, RaftEndpoint localEndpoint) {
        this.restTemplate = restTemplate;
        this.localEndpoint = localEndpoint;
    }

    @Override
    public void send(RaftEndpoint raftEndpoint, RaftMessage raftMessage) {

        if (!(raftEndpoint instanceof ReplicaId replicaId)) {
            log.error("Expecting raftEndpoint of type: ReplicaId , got: {}", raftEndpoint);
            return;
        }

        log.debug(STR."Send message: \{raftMessage} to \{replicaId.getId()}");
        String url = STR."http://\{replicaId.getHost()}/message";
        HttpEntity<ReplicaMessage> request = new HttpEntity<>(ReplicaMessage.from(raftMessage));
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully received from {}", raftEndpoint.getId());
            } else {
                log.error("Error Sending message to {}", raftEndpoint.getId());
            }
        } catch (ResourceAccessException e) {
            log.error("Unreachable node: {} at {}", replicaId.getId(), replicaId.getHost());
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReachable(RaftEndpoint raftEndpoint) {
        log.debug(STR."Pinging \{raftEndpoint.getId()}");

        if (!(raftEndpoint instanceof ReplicaId replicaId)) {
            log.error("Expecting raftEndpoint of type: ReplicaId , got: {}", raftEndpoint);
            return false;
        }

        ResponseEntity<Void> response = null;
        try {
            String url = STR."http://\{replicaId.getHost()}/ping";
            HttpEntity<String> request = new HttpEntity<>(localEndpoint.getId().toString());
            response = restTemplate.postForEntity(url, request, Void.class);
        } catch (RestClientException e) {
            log.error("Error Sending message ping to {}", raftEndpoint.getId());
            return false;
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            log.debug("Successfully received ping from {}", raftEndpoint.getId());
        } else {
            log.error("Error ping Response ping from {}", raftEndpoint.getId());
            return false;
        }
        return true;
    }
}
