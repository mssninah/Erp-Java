package s6.eval.first.crm.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

    @Value("${laravel.url}/api/projects")
    private String projectsUrl;

    private final RestTemplate restTemplate;

    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Project> getAllProjects(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        return restTemplate.exchange(
            projectsUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<Project>>() {}
        ).getBody();
    }
}