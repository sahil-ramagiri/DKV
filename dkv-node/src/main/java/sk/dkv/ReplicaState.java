package sk.dkv;

import io.microraft.statemachine.StateMachine;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class ReplicaState implements StateMachine {

    private LinkedHashMap<String, String> map = new LinkedHashMap<>();

    @Override
    public Object runOperation(long commitIndex, @Nonnull Object operation) {
        requireNonNull(operation);
        if (operation instanceof KVOperation kvOperation) {
            log.info(STR."Running Operation: \{operation}");
            return switch (kvOperation) {
                case KVOperation.GetOperation(String key) -> map.get(key);
                case KVOperation.SetOperation(String key, String value) -> map.put(key, value);
                case KVOperation.ClearOperation(String key) -> map.remove(key);
                case KVOperation.StartNewTermOperation() -> null;
            };
        }

        throw new IllegalArgumentException(STR."Invalid operation: \{operation} of type: \{operation.getClass()} at commit index: \{commitIndex}");
    }

    @Override
    public void takeSnapshot(long l, Consumer<Object> consumer) {
        log.info("Preparing Snapshot with {} entries", map.size());
        consumer.accept(map);
    }

    @Override
    public void installSnapshot(long l, List<Object> list) {
        log.info("Installing Snapshot");
        map.clear();
        for (Object o : list) {
            if (o instanceof Map m) {
                map.putAll(m);
            } else {
                throw new IllegalArgumentException(STR."Invalid chunk data. Got: \{o.getClass()}");
            }
        }
    }

    @Override
    public Object getNewTermOperation() {
        return new KVOperation.StartNewTermOperation();
    }
}
