package sk.dkv.transport;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.microraft.model.message.RaftMessage;
import lombok.Data;
import org.springframework.boot.jackson.JsonMixin;
import sk.dkv.ReplicaId;

@Data
@JsonMixin(ReplicaId.class)
public class ReplicaMessage {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    RaftMessage raftMessage;
    String type;

    public static ReplicaMessage from(RaftMessage raftMessage) {
        ReplicaMessage message = new ReplicaMessage();
        message.setRaftMessage(raftMessage);
        message.setType(raftMessage.getClass().getName());
        return message;
    }
}
