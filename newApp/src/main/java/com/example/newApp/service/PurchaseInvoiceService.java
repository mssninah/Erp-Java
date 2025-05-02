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
public class PurchaseInvoiceService {

    @Value("${erpnext.base-url}")
    private String erpNextBaseUrl;

    private final RestTemplate restTemplate;
    private final HttpSession session;

    public PurchaseInvoiceService(RestTemplate restTemplate, HttpSession session) {
        this.restTemplate = restTemplate;
        this.session = session;
    }

    private String getSidFromSession() {
        return (String) session.getAttribute("sid");
    }

    public List<Map<String, Object>> getPurchaseInvoices() {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }
    
        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/method/erpnext.api.data.get_purchase_invoices")
                .toUriString();
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);
    
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        
        // Extraire le champ "message" qui contient les factures
        List<Map<String, Object>> purchaseInvoices = (List<Map<String, Object>>) response.getBody().get("message");
    
        return purchaseInvoices;
    }
    
    // Obtenir les détails d'une facture d'achat
    public Map<String, Object> getPurchaseInvoiceDetails(String invoiceId) {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/resource/Purchase Invoice/" + invoiceId)
                .build(false)
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    // Action de paiement pour une facture d'achat
    public String payPurchaseInvoice(String invoiceId) {
        String sid = getSidFromSession();
        if (sid == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String url = UriComponentsBuilder.fromHttpUrl(erpNextBaseUrl)
                .path("/api/method/erpnext.api.payment.pay_purchase_invoice")
                .queryParam("invoice_id", invoiceId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "sid=" + sid);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.POST, entity, Map.class);
        return (String) response.getBody().get("message");
    }
}
