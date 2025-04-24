package s6.eval.first.crm.auth;

import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {


    @Value("${laravel.url}/api/login")
    private String loginUrl;

    @GetMapping("/login")
    public String showLoginForm() {
        return "pages/login"; // Affiche le formulaire de login
    }

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthController.class);

    
    @PostMapping("/login")
    public String login(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session,
        RedirectAttributes redirectAttributes, RestTemplate restTemplate
    ) {
        try {
            // 1. Headers + Body
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("email", email, "password", password),
                headers
            );

            // 2. Appel API avec gestion précise du type
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                loginUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
            );

            // 3. Succès
            session.setAttribute("LARAVEL_TOKEN", response.getBody().get("token"));
            logger.debug("Token stocké : {}", session.getAttribute("LARAVEL_TOKEN"));
            return "redirect:/dashboard";

        } catch (HttpClientErrorException e) {
            // 4. Gestion des erreurs HTTP 4xx/5xx
            logger.error("Erreur API - Statut: {} / Body: {}", e.getStatusCode(), e.getResponseBodyAsString());

            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                redirectAttributes.addFlashAttribute("error", "Email ou mot de passe incorrect");
            } else {
                redirectAttributes.addFlashAttribute("error", "Erreur technique (" + e.getStatusCode() + ")");
            }

        
                logger.error("Headers: {}", e.getResponseHeaders());
                logger.error("Body: {}", e.getResponseBodyAsString());
            

        } catch (Exception e) {
            // 5. Erreurs inattendues
            logger.error("Erreur inconnue", e);
            redirectAttributes.addFlashAttribute("error", "Erreur serveur");
        }

        return "redirect:/login";
    }

    private final RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${laravel.url}/api/logout")
    private String logoutUrl;

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        
        if (token != null) {
            try {
                // Appel à l'API Laravel pour invalider le token
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);
                
                restTemplate.exchange(
                    logoutUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    Void.class
                );
                
                redirectAttributes.addFlashAttribute("success", "Déconnexion réussie");
            } catch (Exception e) {
                logger.error("Erreur lors de la déconnexion", e);
                redirectAttributes.addFlashAttribute("error", "Erreur lors de la déconnexion");
            }
        }
        
        // Invalidation de la session Spring
        session.invalidate();
        return "redirect:/login";
    }
}
