package com.example.demo.service;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.QuotationDTO;
import com.example.demo.dto.QuotationItemDTO;
import com.example.demo.dto.WarehouseDTO;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FormulaireService {

    @Value("${erpnext.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public FormulaireService(RestTemplate s){
        this.restTemplate = s;
    }

    public List<QuotationItemDTO>  getItem(String sid){
        String url = baseUrl + "/api/resource/Item?fields=[\"*\"]";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "sid=" + sid);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<JsonNode> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            JsonNode.class
        );


        List<QuotationItemDTO> items = new ArrayList<>();
        if (response.getBody() != null && response.getBody().has("data")) {
            JsonNode data = response.getBody().get("data");
            for (JsonNode item : data) {
                    QuotationItemDTO itemDTO = new QuotationItemDTO();
                    itemDTO.setName(getTextValue(item, "name"));
                    itemDTO.setItem_code(getTextValue(item, "item_code"));
                    itemDTO.setItem_name(getTextValue(item, "item_name"));
                    itemDTO.setQty(getDoubleValue(item, "qty"));
                    itemDTO.setStock_uom(getTextValue(item, "stock_uom"));
                    itemDTO.setRate(getDoubleValue(item, "rate"));
                    itemDTO.setAmount(getDoubleValue(item, "amount"));
                    itemDTO.setDescription(getTextValue(item, "description"));
                    itemDTO.setConversion_factor(getDoubleValue(item, "conversion_factor"));
                    items.add(itemDTO);
                }
            }
            return items;
    }



    public List<WarehouseDTO> getWarehouseDTOs(String sid){
        String url = baseUrl + "/api/resource/Warehouse?fields=[\"*\"]";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "sid=" + sid);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<JsonNode> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            JsonNode.class
        );


        List<WarehouseDTO> warehouse = new ArrayList<>();
        if (response.getBody() != null && response.getBody().has("data")) {
            JsonNode data = response.getBody().get("data");
            for (JsonNode item : data) {
                    WarehouseDTO warehouseDTO = new WarehouseDTO();
                    warehouseDTO.setWarehouse_name(getTextValue(item, "warehouse_name"));
                    warehouse.add(warehouseDTO);
                }
            }
            return warehouse;

    }

    
    private String getTextValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asText() : null;
    }

    private Double getDoubleValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asDouble() : null;
    }


    public void createSupplierQuotation(QuotationDTO quotationDTO, String sid) {
        // Définir les valeurs par défaut
        quotationDTO.setStatus("Draft");
        //quotationDTO.setCompany("Ninah-company");


        // Ajouter la logique pour sauvegarder ou envoyer les données au backend API
        // Exemple : appel à une API Rest pour créer le Supplier Quotation
        saveToBackend(quotationDTO, sid);
    }

    private void saveToBackend(QuotationDTO quotationDTO, String sid) {
        // Implémentation de l'appel API ou de la sauvegarde dans la base de données
        System.out.println("Création du Supplier Quotation : " + quotationDTO);
    }

}
