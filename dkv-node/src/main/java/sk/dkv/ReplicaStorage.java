package sk.dkv;

import io.microraft.model.log.LogEntry;
import io.microraft.model.log.RaftGroupMembersView;
import io.microraft.model.log.SnapshotChunk;
import io.microraft.model.persistence.RaftEndpointPersistentState;
import io.microraft.model.persistence.RaftTermPersistentState;
import io.microraft.persistence.RaftStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ReplicaStorage implements RaftStore {

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
    public void persistLogEntry(LogEntry logEntry) throws IOException {
        log.info(STR."persistLogEntry: \{logEntry}");
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
    public void deleteSnapshotChunks(long l, int i) throws IOException {
        log.info(STR."deleteSnapshotChunks: \{l} , \{i}");
    }

    @Override
    public void flush() throws IOException {
        log.info("flush");
    }
}
