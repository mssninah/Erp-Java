

package com.example.newApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Service
public class SupplierService {

    @Value("${erpnext.base-url}")
    private String erpNextBaseUrl; // L'URL de base de votre ERPNext

    private final RestTemplate restTemplate;
    private final HttpSession session;

    public SupplierService(RestTemplate restTemplate, HttpSession session) {
        this.restTemplate = restTemplate;
        this.session = session;
    }

    // Récupérer le sid de la session
    private String getSidFromSession() {
        return (String) session.getAttribute("sid");
    }

    // Appeler l'API pour récupérer les fournisseurs
    public List<Map<String, Object>> getSuppliers() {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/resource/Supplier")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    // Appeler l'API pour récupérer les données consolidées (quotations et commandes d'achat)
    private Map<String, Object> getSupplierData(String supplier) {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/method/erpnext.api.data.get_supplier_data")
                .queryParam("supplier", supplier)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        return (Map<String, Object>) response.getBody().get("message");
    }

    // Appeler l'API pour récupérer les devis fournisseurs par fournisseur
    public List<Map<String, Object>> getSupplierQuotations(String supplier) {
        Map<String, Object> supplierData = getSupplierData(supplier);
        return (List<Map<String, Object>>) supplierData.get("supplier_quotations");
    }

    // Appeler l'API pour récupérer les Purchase Orders d'un fournisseur
    public List<Map<String, Object>> getPurchaseOrders(String supplier) {
        Map<String, Object> supplierData = getSupplierData(supplier);
        return (List<Map<String, Object>>) supplierData.get("purchase_orders");
    }

    // Appeler l'API pour récupérer les détails d'une Supplier Quotation
    public Map<String, Object> getSupplierQuotationDetails(String quotationId) {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/resource/Supplier Quotation/" + quotationId)
                .build(false)
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    // Appeler l'API pour récupérer les détails d'une commande d'achat (Purchase Order)
    public Map<String, Object> getPurchaseOrderDetails(String purchaseOrderId) {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/resource/Purchase Order/" + purchaseOrderId)
                .build(false)
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }
}