package com.example.demo.controller;

import com.example.demo.dto.QuotationDTO;
import com.example.demo.dto.QuotationItemDTO;
import com.example.demo.dto.SupplierDTO;
import com.example.demo.dto.WarehouseDTO;
import com.example.demo.service.FormulaireService;
import com.example.demo.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/form")
public class FormulaireController {

    private final FormulaireService formulaireService;
    private final SupplierService supplierService;

    public FormulaireController(FormulaireService formulaireService, SupplierService supplierService) {
        this.formulaireService = formulaireService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String showFormulaire(
            @CookieValue(name = "sid", required = true) String sid,
            Model model) {

        List<QuotationItemDTO> items = formulaireService.getItem(sid);
        List<WarehouseDTO> warehouses = formulaireService.getWarehouseDTOs(sid);
        List<SupplierDTO> suppliers = supplierService.getSuppliers(sid);


         // Initialisation d'un QuotationDTO avec des items par défaut
        QuotationDTO quotationDTO = new QuotationDTO();
        quotationDTO.setItems(List.of(new QuotationItemDTO())); 

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("items", items);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("quotationDTO", new QuotationDTO());
        return "form/form";
    }

    @PostMapping("/form")
    public String handleFormSubmission(
            @ModelAttribute QuotationDTO quotationDTO,
            @CookieValue(name = "sid", required = true) String sid) {
        // Appeler le service pour traiter le Supplier Quotation
        formulaireService.createSupplierQuotation(quotationDTO, sid);
        return "redirect:/success";
    }
    
}
