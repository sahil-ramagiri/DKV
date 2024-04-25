package sk.dkvclient;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ClientMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientMain.class).web(WebApplicationType.NONE).run(args);
    }

}
