package sk.dkv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReplicaIdTest {

    @Test
    void testSerialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = """
        {
        "id" : "node1",
        "host" : "localhost:3000"
        }
        """;

        ReplicaId replicaId = objectMapper.readValue(json, ReplicaId.class);

        System.out.println(replicaId);
    }
}