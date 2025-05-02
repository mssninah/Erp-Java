package com.example.newApp.controller;

import com.example.newApp.service.PurchaseInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class PurchaseInvoiceController {

    @Autowired
    private PurchaseInvoiceService purchaseInvoiceService;

    // Afficher la liste des factures d'achat
    @GetMapping("/purchase-invoices")
    public String getPurchaseInvoices(Model model) {
        List<Map<String, Object>> purchaseInvoices = purchaseInvoiceService.getPurchaseInvoices();
        model.addAttribute("purchaseInvoices", purchaseInvoices); // Ajouter à la vue
        return "purchase-invoice-list";  // Retourne la vue purchase-invoice-list.html
    }

    // Afficher les détails d'une facture d'achat
    @GetMapping("/purchase-invoice-details/{id}")
    public String getPurchaseInvoiceDetails(@PathVariable String id, Model model) {
        Map<String, Object> details = purchaseInvoiceService.getPurchaseInvoiceDetails(id);
        model.addAttribute("details", details); // Ajouter à la vue
        return "purchase-invoice-details";  // Retourne la vue purchase-invoice-details.html
    }

    // Effectuer une action de paiement pour une facture
    @GetMapping("/pay-purchase-invoice/{id}")
    public String payPurchaseInvoice(@PathVariable String id, Model model) {
        try {
            String message = purchaseInvoiceService.payPurchaseInvoice(id);
            model.addAttribute("successMessage", message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error processing payment: " + e.getMessage());
        }
        return "redirect:/purchase-invoices";  // Redirige vers la liste des factures après paiement
    }
}
