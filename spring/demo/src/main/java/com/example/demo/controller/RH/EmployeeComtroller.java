package com.example.demo.controller.RH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.RH.EmployeeDTO;
import com.example.demo.dto.RH.SalarySlipDTO;
import com.example.demo.service.RH.EmployeeService;
import com.example.demo.service.RH.SalarySlipService;

@Controller
@RequestMapping("/employee")
public class EmployeeComtroller {
    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public SalarySlipService salarySlipService;
    
    @GetMapping
    public String showEmployeeList(
        @CookieValue(name = "sid", required = true) String sid,
        @RequestParam(value = "search", required = false) String searchQuery,
        Model model
    ) {
        try {
            // Passer la recherche globale au service
            List<EmployeeDTO> employees = employeeService.getEmployee(sid, searchQuery);
            model.addAttribute("employees", employees);
            model.addAttribute("searchQuery", searchQuery); // Ajouter le paramètre au modèle pour réutilisation dans la vue
            return "employee/employee-list";
        } catch (Exception e) {
            List<EmployeeDTO> employees = List.of();
            model.addAttribute("employees", employees);
            model.addAttribute("searchQuery", searchQuery);
            return "employee/employee-list";
        }
    }

}
