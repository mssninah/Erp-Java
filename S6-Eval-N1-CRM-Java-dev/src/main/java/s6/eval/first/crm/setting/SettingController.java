package s6.eval.first.crm.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("/settings/remise")
    public String showDiscountForm(Model model, HttpSession session) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        DiscountRate response = settingService.getDiscountRate(token);
        model.addAttribute("currentRate", response.getTauxremise());
        return "pages/settings/discount-form";
    }

    @PostMapping("/settings/remise")
    public String updateDiscountRate(
        @RequestParam("newRate") double newRate,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");
        DiscountRate response = settingService.updateDiscountRate(token, newRate);

        if (response.getMessage().contains("succès")) {
            redirectAttributes.addFlashAttribute("success", response.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", response.getMessage());
        }
        
        return "redirect:/settings/remise";
    }
}
