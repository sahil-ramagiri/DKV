package sk.dkv;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = KVOperation.GetOperation.class),
        @JsonSubTypes.Type(value = KVOperation.SetOperation.class),
        @JsonSubTypes.Type(value = KVOperation.ClearOperation.class),
        @JsonSubTypes.Type(value = KVOperation.StartNewTermOperation.class)
})
public sealed interface KVOperation {
    record GetOperation(String key) implements KVOperation {};
    record SetOperation(String key, String value) implements KVOperation {};
    record ClearOperation(String key) implements KVOperation {};
    record StartNewTermOperation() implements KVOperation {};
}
