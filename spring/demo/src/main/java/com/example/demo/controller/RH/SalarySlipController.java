package com.example.demo.controller.RH;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.demo.dto.RH.EmployeeDTO;
import com.example.demo.dto.RH.SalaryComponentDTO;
import com.example.demo.dto.RH.SalarySlipDTO;
import com.example.demo.dto.RH.StatistiqueDTO;
import com.example.demo.service.RH.EmployeeService;
import com.example.demo.service.RH.SalaryComponentService;
import com.example.demo.service.RH.SalarySlipService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/salary")
public class SalarySlipController {
    @Autowired
    public SalarySlipService salarySlipService;

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired 
    private SalaryComponentService salaryComponentService;


    @GetMapping("/{EmployeeId}")
    public String showSalarySlipByEmployee(
        @CookieValue(name = "sid", required = true) String sid,
        @PathVariable String EmployeeId,
        Model model
    ){
        try {
            List<SalarySlipDTO> salarys = salarySlipService.getSalarySlip(sid,EmployeeId);
            EmployeeDTO employee = employeeService.getEmployeeByName(sid, EmployeeId);

            model.addAttribute("salarys", salarys);
            model.addAttribute("employee", employee);
            return "salary/salary-list";
        } catch (Exception e) {
            List<SalarySlipDTO> salarys = List.of();
            model.addAttribute("salarys", salarys);
            return "salary/salary-list";
        }
    }

    @GetMapping("/detail/{doctype}/{employee}/{number}")
    public String showDetails(
        @CookieValue(name = "sid", required = true) String sid,
        @PathVariable String doctype,
        @PathVariable String employee,
        @PathVariable String number,
        Model model
    ){
        String SalaryId = doctype + "/" + employee + "/" + number;
        try {
            SalarySlipDTO salary = salarySlipService.getSalarySlipbyName(sid, SalaryId);
            model.addAttribute("salary", salary);
            return "salary/salary-detail";
        } catch (Exception e) {
            SalarySlipDTO salary = new SalarySlipDTO();
            model.addAttribute("salary", salary);
            return "salary/salary-detail";
        }
    }

    @GetMapping("/export/pdf/{doctype}/{employee}/{number}")
    public void exportPdf(
        @PathVariable String doctype,
        @PathVariable String employee,
        @PathVariable String number,
        @CookieValue(name = "sid") String sid,
        HttpServletResponse response
    ) throws Exception {
        String salaryId = doctype + "/" + employee + "/" + number;

        // Récupère les données comme dans showDetails()
        SalarySlipDTO salary = salarySlipService.getSalarySlipbyName(sid, salaryId);

        // Préparer le HTML avec Thymeleaf
        Context context = new Context();
        context.setVariable("salary", salary);
        String html = templateEngine.process("salary/salary-pdf", context);

        // Générer le PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=salary_" + salary.getName() + ".pdf");

        OutputStream outputStream = response.getOutputStream();
        renderer.createPDF(outputStream);
        outputStream.close();
    }

    @GetMapping("/all")
    public String showSalarySlip(
        @CookieValue(name = "sid", required = true) String sid,
        Model model
    ){
        List<SalarySlipDTO> salarys = salarySlipService.getSalarySlip(sid);
        salarys = salarySlipService.completeSalarySlip(sid, salarys);
        List<SalaryComponentDTO> components = salaryComponentService.getSalaryComponent(sid);

        model.addAttribute("salarys", salarys);
        model.addAttribute("components", components);
        return "salary/salary-all";
    }

    @PostMapping("/all")
    public String showSalarySlipMonth(
        @CookieValue(name = "sid", required = true) String sid,
        @RequestParam int mois,
        @RequestParam int annee,
        Model model
    ){
        List<SalarySlipDTO> salarys = salarySlipService.getSalarySlip(sid);
        salarys = salarySlipService.completeSalarySlip(sid, salarys);
        List<SalaryComponentDTO> components = salaryComponentService.getSalaryComponent(sid);
        salarys = salarySlipService.getSalarySlipByMonth(sid, salarys, mois, annee);

        model.addAttribute("salarys", salarys);
        model.addAttribute("components", components);
        return "salary/salary-all";
    }
  





    ///////////////
    @GetMapping("/statistique")
    public String showStatistique(
        @CookieValue(name = "sid", required = true) String sid,
        @RequestParam(name = "annee", required = false) Integer anneeParam,
        Model model
    ) {
        try {
            int annee = (anneeParam != null) ? anneeParam : 2025;
    
            StatistiqueDTO statistique = new StatistiqueDTO();
            statistique.setAnnee(annee);
    
            List<SalarySlipDTO> allSalarys = salarySlipService.getSalarySlip(sid);
            allSalarys = salarySlipService.completeSalarySlip(sid, allSalarys);
    
            Map<Integer, List<SalarySlipDTO>> salarySlipsByMonth = new HashMap<>();
    
            for (int mois = 1; mois <= 12; mois++) {
                List<SalarySlipDTO> salarysOfMonth = salarySlipService.getSalarySlipByMonth(sid, allSalarys, mois, annee);
                salarySlipsByMonth.put(mois, salarysOfMonth);
            }
    
            statistique.setSalarySlips(salarySlipsByMonth);
    
            model.addAttribute("statistique", statistique);
            return "salary/statistiques/list-salary";
    
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("statistique", new StatistiqueDTO());
            return "salary/statistiques/list-salary";
        }
    }
    @PostMapping("/statistiques/details")
    public String showSalaryDetailsByMonth(
        @CookieValue(name = "sid", required = true) String sid,
        @RequestParam int mois,
        @RequestParam int annee,
        Model model
    ) {
        // Récupérer et compléter les salaires
        List<SalarySlipDTO> salaries = salarySlipService.getSalarySlip(sid);
        salaries = salarySlipService.completeSalarySlip(sid, salaries);
        salaries = salarySlipService.getSalarySlipByMonth(sid, salaries, mois, annee);

        // Formater le mois et l'année pour l'affichage
        String monthYear = String.format("%02d/%d", mois, annee);

        // Ajouter les données au modèle
        model.addAttribute("salaries", salaries);
        model.addAttribute("monthYear", monthYear);

        return "salary/statistiques/details-salary";
    }
    

}