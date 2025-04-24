package s6.eval.first.crm.importcsv;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class ImportController {

    private final RestTemplate restTemplate;

    @Value("${laravel.api.url}/import-csv")
    private String LARAVEL_API_URL;

    public ImportController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @GetMapping("/import")
    public String showImportForm() {
        return "pages/import/import-form";
    }


    @PostMapping("/import")
    public String handleFileUpload(@RequestParam("csvFile") MultipartFile file, Model model, HttpSession session) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Veuillez sélectionner un fichier CSV");
            return "pages/import/import-form";
        }

        try {
            // Créer un fichier temporaire
            File tempFile = Files.createTempFile("upload", ".csv").toFile();
            file.transferTo(tempFile);

            // Préparer la requête multipart
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("csv_file", new FileSystemResource(tempFile));

            String token  = (String) session.getAttribute("LARAVEL_TOKEN");
            // En-têtes de la requête
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(token);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Envoi à l'API Laravel
            ResponseEntity<String> response = restTemplate.exchange(
                LARAVEL_API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            // Traitement de la réponse
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("success", true);
                model.addAttribute("message", "Fichier importé avec succès !");
                model.addAttribute("response", response.getBody());
            } else {
                model.addAttribute("error", true);
                model.addAttribute("message", "Erreur lors de l'import: " + response.getBody());
            }

            // Nettoyage du fichier temporaire
            tempFile.delete();

        } catch (IOException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Erreur de traitement du fichier: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Erreur de communication avec l'API: " + e.getMessage());
        }

        return "pages/import/import-form"; // Template de résultat
    }
}