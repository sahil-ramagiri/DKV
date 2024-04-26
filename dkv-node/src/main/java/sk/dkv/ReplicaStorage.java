package sk.dkv;

import io.microraft.model.log.LogEntry;
import io.microraft.model.log.RaftGroupMembersView;
import io.microraft.model.log.SnapshotChunk;
import io.microraft.model.persistence.RaftEndpointPersistentState;
import io.microraft.model.persistence.RaftTermPersistentState;
import io.microraft.persistence.RaftStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sk.dkv.config.ReplicaConfig;

import java.io.IOException;
import java.util.List;

//@Component
@Slf4j
public class ReplicaStorage implements RaftStore {

    private final ReplicaConfig replicaConfig;

    public ReplicaStorage(ReplicaConfig replicaConfig) {
        this.replicaConfig = replicaConfig;
    }

    @Override
    public void persistAndFlushLocalEndpoint(RaftEndpointPersistentState raftEndpointPersistentState) throws IOException {
        log.info(STR."persistAndFlushLocalEndpoint: \{raftEndpointPersistentState}");
    }

    @Override
    public void persistAndFlushInitialGroupMembers(RaftGroupMembersView raftGroupMembersView) throws IOException {
        log.info(STR."persistAndFlushInitialGroupMembers: \{raftGroupMembersView}");

    }

    @Override
    public void persistAndFlushTerm(RaftTermPersistentState raftTermPersistentState) throws IOException {
        log.info(STR."persistAndFlushTerm: \{raftTermPersistentState}");

    }

    @Override
    public void persistLogEntries(List<LogEntry> list) throws IOException {
        log.info(STR."persistLogEntries: \{list}");
    }

    @Override
    public void persistSnapshotChunk(SnapshotChunk snapshotChunk) throws IOException {
        log.info(STR."persistSnapshotChunk: \{snapshotChunk}");
    }

    @Override
    public void truncateLogEntriesFrom(long l) throws IOException {
        log.info(STR."truncateLogEntriesFrom: \{l}");
    }

    @Override
    public void truncateLogEntriesUntil(long l) throws IOException {

    }

    @Override
    public void deleteSnapshotChunks(long l, int i) throws IOException {
        log.info(STR."deleteSnapshotChunks: \{l} , \{i}");
    }

    @Override
    public void flush() throws IOException {
        log.info("flush");
    }
}
