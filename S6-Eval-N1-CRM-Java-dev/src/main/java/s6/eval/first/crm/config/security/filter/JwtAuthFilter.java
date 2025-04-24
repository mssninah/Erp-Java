package s6.eval.first.crm.config.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class JwtAuthFilter implements Filter {
    
    @Value("${laravel.url}/api/users") // Endpoint de validation de token
    private String LARAVEL_API_USER_VALIDATION;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Injection via constructeur
    public JwtAuthFilter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        // Exclusion des routes publiques
        if (isPublicRoute(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        String token = extractToken(session);

        if (!isTokenValid(token)) {
            httpResponse.sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicRoute(String uri) {
        return uri.equals("/login") 
            || uri.startsWith("/css/") 
            || uri.startsWith("/js/")
            || uri.startsWith("/error")
            || uri.startsWith("/assets/")
            || uri.startsWith("/images/");
    }

    private String extractToken(HttpSession session) {
        return session != null ? (String) session.getAttribute("LARAVEL_TOKEN") : null;
    }

    private boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) return false;

        try {

            // 2. Validation via l'API Laravel
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8000/api/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
            );
            // 3. Vérification de la réponse
            if (response.getStatusCode().is2xxSuccessful()) {
                // Lire comme une liste de maps
                List<Map<String, Object>> users = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                System.out.println(!users.isEmpty() && users.get(0).containsKey("external_id"));
                // Vérifier si la liste contient au moins un utilisateur
                return !users.isEmpty() && users.get(0).containsKey("external_id");
            }
            
        } catch (Exception e) {
            /* logger.error("Erreur de validation du token", e); */
            e.printStackTrace();
        }
        
        return false;
    }
}