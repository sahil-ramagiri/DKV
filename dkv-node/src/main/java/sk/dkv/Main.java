package sk.dkv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import sk.dkv.config.ReplicaConfig;
import sk.dkv.config.ReplicaConfigParser;

import java.util.Collections;

@SpringBootApplication
@Slf4j
@EnableAsync
public class Main {

    public static void main(String[] args) {
        ReplicaConfig replicaConfig = ReplicaConfigParser.parseArgs(args);

        SpringApplication app = new SpringApplication(Main.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", replicaConfig.ports().get(replicaConfig.index())));
        app.run(args);
    }

    @Bean
    public ReplicaConfig replicaConfig(ApplicationArguments arguments) {
        return ReplicaConfigParser.parseArgs(arguments.getSourceArgs());
    }
}