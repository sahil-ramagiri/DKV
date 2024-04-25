package sk.dkv;

import io.microraft.RaftEndpoint;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import sk.dkv.config.ReplicaConfig;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReplicaId implements RaftEndpoint {

    private String id;
    private String host;

    public ReplicaId(String id, int port) {
        this.id = id;
        this.host = STR."localhost:\{port}";
    }

    public static ReplicaId getOwn(ReplicaConfig replicaConfig) {
        return new ReplicaId(STR."node\{replicaConfig.index()}", replicaConfig.ports().get(replicaConfig.index()));
    }

    public static List<RaftEndpoint> getAll(ReplicaConfig replicaConfig) {
        ArrayList<RaftEndpoint> endpoints = new ArrayList<>();
        for (int i = 0; i < replicaConfig.ports().size(); i++) {
            endpoints.add(new ReplicaId(STR."node\{i}", replicaConfig.ports().get(i)));
        }
        return endpoints;
    }

    @Override
    @NonNull
    public Object getId() {
        return id;
    }
}
