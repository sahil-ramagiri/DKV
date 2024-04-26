package sk.dkv.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.microraft.RaftEndpoint;
import io.microraft.RaftNode;
import io.microraft.model.log.LogEntry;
import io.microraft.persistence.RaftStore;
import io.microraft.persistence.RestoredRaftState;
import io.microraft.statemachine.StateMachine;
import io.microraft.store.sqlite.RaftSqliteStore;
import io.microraft.transport.Transport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import sk.dkv.ReplicaId;
import sk.dkv.model.ModelSerDe;
import sk.dkv.model.ReplicaRaftModelFactory;
import sk.dkv.model.log.LogEntryOrBuilder;

import java.nio.file.Path;
import java.util.Optional;

@Configuration
@Slf4j
public class ReplicaBootStrap {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        SimpleModule module = new SimpleModule("CustomModel", Version.unknownVersion());
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(RaftEndpoint.class, ReplicaId.class);
        resolver.addMapping(LogEntry.class, LogEntryOrBuilder.class);
        module.setAbstractTypes(resolver);

        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(module);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RaftStore raftStore(ReplicaConfig replicaConfig, ModelSerDe modelSerDe, ReplicaId replicaId) {
        Path path = replicaConfig.dataDir().resolve(STR."\{replicaId.getId()}.sqlite");
        return RaftSqliteStore.create(path.toFile(), new ReplicaRaftModelFactory(), modelSerDe);
    }

    @Bean
    RaftEndpoint raftEndpoint(ReplicaConfig replicaConfig) {
        return ReplicaId.getOwn(replicaConfig);
    }

    @Bean
    RaftNode raftNode(ReplicaConfig replicaConfig, StateMachine stateMachine, Transport transport, RaftStore raftStore, RaftEndpoint raftEndpoint) {

        RaftNode.RaftNodeBuilder builder = RaftNode.newBuilder().setGroupId("default").setTransport(transport).setStateMachine(stateMachine).setStore(raftStore).setModelFactory(new ReplicaRaftModelFactory());

        if (raftStore instanceof RaftSqliteStore sqliteStore) {
            Optional<RestoredRaftState> raftStateOpt = sqliteStore.getRestoredRaftState(true);
            if (raftStateOpt.isPresent()) {
                RestoredRaftState restoredRaftState = raftStateOpt.get();
                log.info("restored local endpoint: {}, voting: {}, initial group members: {}, term: {}, voted for: {}", raftEndpoint.getId(), restoredRaftState.getLocalEndpointPersistentState().isVoting(), restoredRaftState.getInitialGroupMembers(), restoredRaftState.getTermPersistentState().getTerm(), restoredRaftState.getTermPersistentState().getVotedFor());
                builder.setRestoredState(restoredRaftState);
                return builder.build();
            }
        }
        builder.setLocalEndpoint(raftEndpoint).setInitialGroupMembers(ReplicaId.getAll(replicaConfig));
        return builder.build();
    }


    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}
