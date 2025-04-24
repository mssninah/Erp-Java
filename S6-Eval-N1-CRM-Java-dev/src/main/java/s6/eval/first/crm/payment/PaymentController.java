package s6.eval.first.crm.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import s6.eval.first.crm.payment.stat.jour.PaymentDayDetail;
import s6.eval.first.crm.payment.stat.service.PaymentStatisticsService;
import s6.eval.first.crm.payment.stat.source.PaymentSourceDetail;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentStatisticsService paymentStatisticsService;

    public PaymentController(PaymentService paymentService,PaymentStatisticsService paymentStatisticsService) {
        this.paymentService = paymentService;
        this.paymentStatisticsService = paymentStatisticsService;
    }

    @GetMapping("/payments/clients/{clientId}")
    public String getClientPayments(@PathVariable Long clientId, Model model, HttpSession session) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        model.addAttribute("payments", paymentService.getClientPayments(clientId, token));
        return "pages/payment/payment-client-liste";
    }

    @GetMapping("/payments/{externalId}")
    public String showUpdateForm(@PathVariable String externalId, Model model, HttpSession session) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        System.out.println("\t\t\t\t\t"+paymentService.getPaymentByExternalId(externalId, token)+"\t\t\t\t\t");
        model.addAttribute("payment", paymentService.getPaymentByExternalId(externalId, token));
        return "pages/payment/payment-update";
    }

    @PostMapping("/payments/{externalId}")
    public String updatePayment(
        @PathVariable String externalId,
        @ModelAttribute PaymentUpdateRequest request,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        ResponseEntity<?> response = paymentService.updatePayment(externalId, request, token);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", "Paiement mis à jour");
        } else {
            redirectAttributes.addFlashAttribute("error", response.getBody());
        }
        
        return "redirect:/payments/" + externalId;
    }

    @GetMapping("/payments/delete/{externalId}")
    public String deletePayment(
        @PathVariable String externalId,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        ResponseEntity<?> response = paymentService.deletePayment(externalId, token);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", "Paiement mis à jour");
        } else {
            redirectAttributes.addFlashAttribute("error", response.getBody());
        }
        
        return "redirect:/dashboard";
    }


    @GetMapping("/payments/days/{date}")
    public String getDailyPayments(
        @PathVariable String date, 
        Model model, 
        HttpSession session
    ) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        PaymentDayDetail details = paymentStatisticsService.getDailyPaymentsDetails(date, token);
        
        model.addAttribute("payments", details.getPayments());
        model.addAttribute("dailyTotal", details.getDailyTotalReel());
        model.addAttribute("selectedDate", date);
        
        return "pages/payment/payment-day-details";
    }

    @GetMapping("/payments/sources/{source}")
    public String getPaymentsBySource(
        @PathVariable String source,
        Model model,
        HttpSession session
    ) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        PaymentSourceDetail details = paymentStatisticsService.getPaymentsBySource(source, token);
        
        model.addAttribute("payments", details.getPayments());
        model.addAttribute("paymentSource", source);
        model.addAttribute("totalAmount", details.getTotalReel());
        
        return "pages/payment/payment-source-details";
    }
}
