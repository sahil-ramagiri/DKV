package sk.dkvclient;

public record KVRequest(Op operation, String key, String value) {

    public static KVRequest get(String key) {
        return new KVRequest(Op.GET, key, null);
    }

    public static KVRequest set(String key, String value) {
        return new KVRequest(Op.SET, key, value);
    }

    public static KVRequest clear(String key) {
        return new KVRequest(Op.CLEAR, key, null);
    }

    public enum Op {
        GET,
        SET,
        CLEAR
    }
}

