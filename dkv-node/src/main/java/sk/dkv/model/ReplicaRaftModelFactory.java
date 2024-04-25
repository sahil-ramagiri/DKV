package sk.dkv.model;

import io.microraft.model.impl.DefaultRaftModelFactory;
import io.microraft.model.log.LogEntry;
import jakarta.annotation.Nonnull;
import sk.dkv.model.log.LogEntryOrBuilder;

public class ReplicaRaftModelFactory extends DefaultRaftModelFactory {
    @Override
    @Nonnull
    public LogEntry.LogEntryBuilder createLogEntryBuilder() {
        return new LogEntryOrBuilder();
    }


}
