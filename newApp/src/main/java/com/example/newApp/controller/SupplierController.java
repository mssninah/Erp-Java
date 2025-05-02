package com.example.newApp.controller;

import com.example.newApp.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // Afficher la liste des fournisseurs dans le select
    @GetMapping("/suppliers")
    public String getSuppliers(Model model) {
        List<Map<String, Object>> suppliers = supplierService.getSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "supplier-list";  // Retourne la vue supplier-list.html
    }

    // Afficher les Supplier Quotations pour un fournisseur sélectionné
    @GetMapping("/supplier-quotations")
    public String getSupplierQuotations(@RequestParam String supplier, Model model) {
        List<Map<String, Object>> quotations = supplierService.getSupplierQuotations(supplier);
        model.addAttribute("quotations", quotations);
        return "quotation-list";  // Retourne la vue quotation-list.html
    }

    // Afficher les détails d'une Supplier Quotation
    @GetMapping("/supplier-quotations/{id}")
    public String getSupplierQuotationDetails(@PathVariable String id, Model model) {
        Map<String, Object> details = supplierService.getSupplierQuotationDetails(id);
        model.addAttribute("details", details);
        return "quotation-details";  // Retourne la vue quotation-details.html
    }

    // Afficher la liste des commandes d'achat pour un fournisseur
    @GetMapping("/purchase-orders")
    public String getPurchaseOrders(@RequestParam String supplier, Model model) {
        List<Map<String, Object>> purchaseOrders = supplierService.getPurchaseOrders(supplier);
        model.addAttribute("purchaseOrders", purchaseOrders); // Ajouter à la vue
        return "purchase-orders";  // Retourne la vue purchase-orders.html
    }

    // Afficher les détails d'une commande d'achat
    @GetMapping("/purchase-order-details/{id}")
    public String getPurchaseOrderDetails(@PathVariable String id, Model model) {
        Map<String, Object> details = supplierService.getPurchaseOrderDetails(id);
        model.addAttribute("details", details);
        return "purchase-order-details";  // Retourne la vue purchase-order-details.html
    }

}
