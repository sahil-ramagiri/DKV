package sk.dkv.config;

import java.nio.file.Path;
import java.util.List;

public record ReplicaConfig(
        List<Integer> ports,
        Integer index,
        Path dataDir
) {
}
