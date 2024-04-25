package sk.dkv.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.microraft.RaftEndpoint;
import io.microraft.RaftNode;
import io.microraft.model.impl.log.DefaultLogEntryOrBuilder;
import io.microraft.model.log.LogEntry;
import io.microraft.persistence.RaftStore;
import io.microraft.statemachine.StateMachine;
import io.microraft.transport.Transport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import sk.dkv.ReplicaId;
import sk.dkv.config.ReplicaConfig;

@Configuration
public class ReplicaBootStrap {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        SimpleModule module = new SimpleModule("CustomModel", Version.unknownVersion());
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(RaftEndpoint.class, ReplicaId.class);
        resolver.addMapping(LogEntry.class, DefaultLogEntryOrBuilder.class);
        module.setAbstractTypes(resolver);

        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    RaftEndpoint raftEndpoint(ReplicaConfig replicaConfig) {
        return ReplicaId.getOwn(replicaConfig);
    }

    @Bean
    RaftNode raftNode(ReplicaConfig replicaConfig, StateMachine stateMachine, Transport transport, RaftStore raftStore, RaftEndpoint raftEndpoint) {

        return RaftNode.newBuilder()
                .setGroupId("default")
                .setLocalEndpoint(raftEndpoint)
                .setInitialGroupMembers(ReplicaId.getAll(replicaConfig))
                .setTransport(transport)
                .setStateMachine(stateMachine)
                .setStore(raftStore)
                .build();
    }


    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}
