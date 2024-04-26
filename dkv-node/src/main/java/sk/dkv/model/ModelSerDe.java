package sk.dkv.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.microraft.RaftEndpoint;
import io.microraft.model.impl.log.DefaultRaftGroupMembersViewOrBuilder;
import io.microraft.model.impl.log.DefaultSnapshotChunkOrBuilder;
import io.microraft.model.impl.persistence.DefaultRaftEndpointPersistentStateOrBuilder;
import io.microraft.model.impl.persistence.DefaultRaftTermPersistentStateOrBuilder;
import io.microraft.model.log.LogEntry;
import io.microraft.model.log.RaftGroupMembersView;
import io.microraft.model.log.SnapshotChunk;
import io.microraft.model.persistence.RaftEndpointPersistentState;
import io.microraft.model.persistence.RaftTermPersistentState;
import io.microraft.persistence.RaftStoreSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sk.dkv.ReplicaId;
import sk.dkv.model.log.LogEntryOrBuilder;

@Component
@Slf4j
public class ModelSerDe implements RaftStoreSerializer {

    private final ObjectMapper objectMapper;

    public ModelSerDe(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Serializer<RaftGroupMembersView> raftGroupMembersViewSerializer() {
        return new Serializer<RaftGroupMembersView>() {
            @SneakyThrows
            @Override
            public byte[] serialize(RaftGroupMembersView element) {
                log.debug("ser: RaftGroupMembersView");
                return objectMapper.writeValueAsBytes(element);
            }

            @Override
            @SneakyThrows
            public RaftGroupMembersView deserialize(byte[] element) {
                log.debug("deser: RaftGroupMembersView");
                return objectMapper.readValue(element, DefaultRaftGroupMembersViewOrBuilder.class);
            }
        };
    }

    @Override
    public Serializer<RaftEndpoint> raftEndpointSerializer() {
        return new Serializer<RaftEndpoint>() {
            @Override
            @SneakyThrows
            public byte[] serialize(RaftEndpoint element) {
                log.debug("ser: RaftEndpoint");
                return objectMapper.writeValueAsBytes(element);
            }

            @Override
            @SneakyThrows
            public RaftEndpoint deserialize(byte[] element) {
                log.debug("deser: RaftEndpoint");
                return objectMapper.readValue(element, ReplicaId.class);
            }
        };
    }

    @Override
    public Serializer<LogEntry> logEntrySerializer() {
        return new Serializer<LogEntry>() {
            @Override
            @SneakyThrows
            public byte[] serialize(LogEntry element) {
                log.debug("ser: LogEntry");
                return objectMapper.writeValueAsBytes(element);
            }

            @Override
            @SneakyThrows
            public LogEntry deserialize(byte[] element) {
                log.debug("deser: LogEntry");
                return objectMapper.readValue(element, LogEntryOrBuilder.class);
            }
        };
    }

    @Override
    public Serializer<SnapshotChunk> snapshotChunkSerializer() {
        return new Serializer<SnapshotChunk>() {
            @Override
            @SneakyThrows
            public byte[] serialize(SnapshotChunk element) {
                log.debug("ser: SnapshotChunk");
                return objectMapper.writeValueAsBytes(element);
            }

            @Override
            @SneakyThrows
            public SnapshotChunk deserialize(byte[] element) {
                log.debug("deser: SnapshotChunk");
                return objectMapper.readValue(element, DefaultSnapshotChunkOrBuilder.class);
            }
        };
    }

    @Override
    public Serializer<RaftEndpointPersistentState> raftEndpointPersistentStateSerializer() {
        return new Serializer<RaftEndpointPersistentState>() {
            @Override
            @SneakyThrows
            public byte[] serialize(RaftEndpointPersistentState element) {
                log.debug("ser: RaftEndpointPersistentState");
                return objectMapper.writeValueAsBytes(element);
            }

            @Override
            @SneakyThrows
            public RaftEndpointPersistentState deserialize(byte[] element) {
                log.debug("deser: RaftEndpointPersistentState");
                return objectMapper.readValue(element, DefaultRaftEndpointPersistentStateOrBuilder.class);
            }
        };
    }

    @Override
    public Serializer<RaftTermPersistentState> raftTermPersistentState() {
        return new Serializer<RaftTermPersistentState>() {
            @Override
            @SneakyThrows
            public byte[] serialize(RaftTermPersistentState element) {
                log.debug("ser: RaftTermPersistentState");
                return objectMapper.writeValueAsBytes(element);
            }

            @Override
            @SneakyThrows
            public RaftTermPersistentState deserialize(byte[] element) {
                log.debug("deser: RaftTermPersistentState");
                return objectMapper.readValue(element, DefaultRaftTermPersistentStateOrBuilder.class);
            }
        };
    }
}
