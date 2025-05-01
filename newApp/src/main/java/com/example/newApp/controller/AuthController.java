package com.example.newApp.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    // Afficher la page de connexion
    @GetMapping("/")
    public String showLoginPage() {
        return "login";  // Cela va chercher login.html dans src/main/resources/templates
    }



@PostMapping("/login")
public String login(@RequestParam("usr") String usr, @RequestParam("pwd") String pwd, Model model, HttpSession session) {
    try {
        String url = "http://erpnext.localhost:8000/api/method/login";
        RestTemplate restTemplate = new RestTemplate();

        // Préparer le corps JSON
        Map<String, String> body = new HashMap<>();
        body.put("usr", usr);
        body.put("pwd", pwd);

        // Préparer les headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // Faire la requête POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCodeValue() == 200) {
            // Extraire les cookies de l'en-tête
            List<String> setCookieHeaders = response.getHeaders().get("Set-Cookie");

            if (setCookieHeaders != null) {
                String sid = null;
                String fullName = null;

                for (String cookie : setCookieHeaders) {
                    System.out.println(cookie);
                    // Rechercher "sid="
                    if (cookie.startsWith("sid=")) {
                        sid = cookie.split(";")[0].substring(4); // Extraire la valeur de sid
                    }
                    if (cookie.startsWith("full_name=")) {
                        fullName = cookie.split(";")[0].substring(10); // Extraire la valeur de full_name
                    }
                }

                if (sid != null) {
                    // Stocker `sid` dans la session
                    session.setAttribute("sid", sid);
                }
                if (fullName != null) {
                    session.setAttribute("fullName", fullName);
                }

                // Connexion réussie : redirection vers la page d'accueil
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Aucun cookie de session n'a été retourné.");
                return "login";
            }
        } else {
            model.addAttribute("error", "Identifiants incorrects. Veuillez réessayer.");
            return "login";
        }
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Une erreur est survenue lors de la connexion.");
        return "login";
    }
}

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        // Récupérer `sid` et `fullName` de la session
        String sid = (String) session.getAttribute("sid");
        String fullName = (String) session.getAttribute("fullName");

        if (sid != null) {
            model.addAttribute("sid", sid);
        }
        if (fullName != null) {
            model.addAttribute("fullName", fullName);
        }

        return "home";  // Cela va chercher home.html dans src/main/resources/templates
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalider la session
        session.invalidate();
        return "redirect:/";  // Rediriger vers la page de connexion
    }


}