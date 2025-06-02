package com.example.demo.controller.RH;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("import")
public class ImportController {

    /**
     * Displays the import form page.
     * The corresponding HTML file should be located at: src/main/resources/templates/import/import.html
     */
    @RequestMapping
    public String showImportForm() {
        return "import/import"; // Assumes a Thymeleaf template named import.html inside the "import" folder
    }

    /**
     * Processes the uploaded files and displays results (success or error) on the same page.
     */
    @PostMapping
    public String processImport(MultipartFile file1, MultipartFile file2, MultipartFile file3, Model model) {
        StringBuilder message = new StringBuilder();
        boolean hasError = false;

        if (file1 == null || file1.isEmpty()) {
            message.append("File 1 is missing or empty.<br>");
            hasError = true;
        } else {
            message.append("File 1 imported successfully: ").append(file1.getOriginalFilename()).append("<br>");
        }

        if (file2 == null || file2.isEmpty()) {
            message.append("File 2 is missing or empty.<br>");
            hasError = true;
        } else {
            message.append("File 2 imported successfully: ").append(file2.getOriginalFilename()).append("<br>");
        }

        if (file3 == null || file3.isEmpty()) {
            message.append("File 3 is missing or empty.<br>");
            hasError = true;
        } else {
            message.append("File 3 imported successfully: ").append(file3.getOriginalFilename()).append("<br>");
        }

        if (hasError) {
            model.addAttribute("error", message.toString());
        } else {
            model.addAttribute("success", "All files were imported successfully!");
        }

        return "import/import"; // Reloads the same page with the result messages
    }
}
