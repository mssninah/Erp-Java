package s6.eval.first.crm.invoice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String listInvoices(HttpSession session, Model model) {
        // Récupération du token depuis la session
        String token  = (String) session.getAttribute("LARAVEL_TOKEN");
        model.addAttribute("invoices", invoiceService.getInvoices(token));
        return "pages/invoice/invoice-list";
    }

    @PostMapping("/apply-discount/{externalId}")
    public String applyDiscount(@PathVariable String externalId, HttpSession session) {
        String token  = (String) session.getAttribute("LARAVEL_TOKEN");
        invoiceService.applyDiscount(externalId,token);
        return "redirect:/invoices";
    }

    @PostMapping("/remove-discount/{externalId}")
    public String removeDiscount(@PathVariable String externalId, HttpSession session) {
        String token  = (String) session.getAttribute("LARAVEL_TOKEN");
        invoiceService.removeDiscount(externalId,token);
        return "redirect:/invoices";
    }
}