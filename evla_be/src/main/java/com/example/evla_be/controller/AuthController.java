package com.example.evla_be.controller;

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

@Controller
public class AuthController {

    // Afficher la page de connexion
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Cela va chercher login.html dans src/main/resources/templates
    }

    // Gérer la soumission du formulaire de connexion
    @PostMapping("/login")
    public String login(@RequestParam("usr") String usr, @RequestParam("pwd") String pwd) {
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

            // Vérifier la réponse
            if (response.getStatusCodeValue() == 200) {
                // Connexion réussie : redirection vers la page d'accueil
                return "redirect:/home";
            } else {
                System.err.println("Reponse servveur: " + response);
                // Identifiants incorrects : message d'erreur
                return "redirect:/login?error=true";
            }

        } catch (Exception e) {
            // Gestion des erreurs
            e.printStackTrace();
            return "redirect:/login?error=true";
        }
    }

    // Route pour afficher la page d'accueil
    @GetMapping("/home")
    public String showHomePage() {
        return "home";  // Cela va chercher home.html dans src/main/resources/templates
    }
}