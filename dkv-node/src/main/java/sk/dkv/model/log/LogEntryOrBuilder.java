package sk.dkv.model.log;

import io.microraft.model.log.LogEntry;
import lombok.Builder;
import sk.dkv.KVOperation;


@Builder
public class LogEntryOrBuilder implements LogEntry, LogEntry.LogEntryBuilder {

    private int term;
    private long index;
    private KVOperation operation;

    private final LogEntryOrBuilderBuilder builder = new LogEntryOrBuilderBuilder();

    @Override
    public LogEntry.LogEntryBuilder setIndex(long index) {
        builder.index(index);
        return this;
    }

    @Override
    public LogEntry.LogEntryBuilder setTerm(int term) {
        builder.term(term);
        return this;
    }

    @Override
    public LogEntry.LogEntryBuilder setOperation(Object operation) {
        if (!(operation instanceof KVOperation kvOperation))
            throw new IllegalArgumentException(STR."Invalid operation: \{operation}");
        builder.operation(kvOperation);
        return this;
    }

    @Override
    public LogEntry build() {
        return builder.build();
    }

    @Override
    public long getIndex() {
        return index;
    }

    @Override
    public int getTerm() {
        return term;
    }

    @Override
    public Object getOperation() {
        return operation;
    }
}
