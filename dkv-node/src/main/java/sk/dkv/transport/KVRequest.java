package sk.dkv.transport;

public record KVRequest(Op operation, String key, String value) {
    public enum Op {
        GET,
        SET,
        CLEAR
    }
}

