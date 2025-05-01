package com.example.newApp.controller;

import com.example.newApp.dto.SupplierDTO;
import com.example.newApp.dto.ItemDTO;
import com.example.newApp.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/suppliers-and-items")
    public Map<String, List<?>> fetchSuppliersAndItems() {
        
        // Récupérer les fournisseurs et articles depuis le service
        Map<String, List<?>> data = dataService.getSuppliersAndItems();

        // Afficher les données dans la console
        System.out.println("Données récupérées : " + data);

        // Retourner les données au client
        return data;
    }
}
