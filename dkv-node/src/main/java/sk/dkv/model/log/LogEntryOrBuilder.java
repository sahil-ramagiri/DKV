package sk.dkv.model.log;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.microraft.model.log.LogEntry;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.dkv.KVOperation;

@NoArgsConstructor
@Data
public class LogEntryOrBuilder implements LogEntry, LogEntry.LogEntryBuilder {

    private int term;
    private long index;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    private KVOperation operation;

    private String type;

    @Override
    public LogEntry.LogEntryBuilder setIndex(long index) {
        this.index = index;
        return this;
    }

    @Override
    public LogEntry.LogEntryBuilder setTerm(int term) {
        this.term = term;
        return this;
    }

    @Override
    public LogEntry.LogEntryBuilder setOperation(Object operation) {
        if (!(operation instanceof KVOperation kvOperation)) {
            throw new IllegalArgumentException(STR."Invalid operation: \{operation}");
        }
        this.operation = kvOperation;
        this.type = kvOperation.getClass().getName();
        return this;
    }

    @Override
    public LogEntry build() {
        return this;
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
