package s6.eval.first.crm.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TaskService {

    @Value("${laravel.url}/api/tasks")
    private String tasksUrl;

    private final RestTemplate restTemplate;

    public TaskService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Task> getAllTasks(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        return restTemplate.exchange(
            tasksUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<Task>>() {}
        ).getBody();
    }
}
