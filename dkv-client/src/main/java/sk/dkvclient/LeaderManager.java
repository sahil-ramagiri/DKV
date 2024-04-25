package sk.dkvclient;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Data
@Component
public class LeaderManager {

    private final RestTemplate restTemplate;
    private List<String> hosts;
    private int currentHost;

    public LeaderManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    void setHosts(List<String> hosts) {
        this.hosts = hosts;
        this.currentHost = 0;
    }

    public String getRequest(String key) {
        return invoke(KVRequest.get(key));
    }

    public String setRequest(String key, String value) {
        return invoke(KVRequest.set(key, value));
    }

    public String clearRequest(String key) {
        return invoke(KVRequest.clear(key));
    }

    private String invoke(KVRequest request) {
        for (int i = 0; i < hosts.size(); i++) {
            int index = (currentHost + i) % hosts.size();
            log.info("Index: {}, currentHost: {}", index, currentHost);
            String url = STR."http://\{hosts.get(index)}/kv";
            RequestEntity<KVRequest> requestEntity = RequestEntity.post(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(request);
            try {
                ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    currentHost = index;
                    return response.getBody();
                } else {
                    log.error("Invalid Response {}", response);
                }
            } catch (RestClientException e) {
                log.warn("Invalid response from {}. Trying another server", url);
            }
        }
        throw new RuntimeException("Server Unreachable");
    }
}
