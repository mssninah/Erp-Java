package s6.eval.first.crm.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import s6.eval.first.crm.client.Client;
import s6.eval.first.crm.client.ClientService;
import s6.eval.first.crm.invoice.stat.product.ProductStatistics;
import s6.eval.first.crm.invoice.stat.service.InvoiceStatisticsService;
import s6.eval.first.crm.invoice.stat.status.StatusStatistics;
import s6.eval.first.crm.offer.service.OfferStatisticsService;
import s6.eval.first.crm.payment.stat.jour.PaymentDayStatistics;
import s6.eval.first.crm.payment.stat.service.PaymentStatisticsService;
import s6.eval.first.crm.payment.stat.source.PaymentSourceStatistics;
import s6.eval.first.crm.project.Project;
import s6.eval.first.crm.project.ProjectService;
import s6.eval.first.crm.task.Task;
import s6.eval.first.crm.task.TaskService;

@Controller
public class DashboardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OfferStatisticsService offerStatsService;

    @Autowired
    private PaymentStatisticsService paymentStatsService;

    @Autowired
    private InvoiceStatisticsService invoiceStatsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String token = (String) session.getAttribute("LARAVEL_TOKEN");

        List<Client> clients = clientService.getAllClients(token);
        model.addAttribute("nbclients", clients.size());

        List<Project> projects = projectService.getAllProjects(token);
        model.addAttribute("nbprojects", projects.size());

        List<Task> tasks =taskService.getAllTasks(token);
        model.addAttribute("nbtasks", tasks.size());

        System.out.println(offerStatsService.getGeneralStatistics(token));

        // Ajout des statistiques
        model.addAttribute("generalStats", offerStatsService.getGeneralStatistics(token));
        model.addAttribute("clientStats", offerStatsService.getClientStatistics(token));
        model.addAttribute("userStats", offerStatsService.getUserStatistics(token));

        // Statistiques paiements
        model.addAttribute("paymentStats", paymentStatsService.getClientPaymentStats(token));

        // Ajoutez ces lignes
        ProductStatistics productStats = invoiceStatsService.getProductStatistics(token);
        StatusStatistics statusStats = invoiceStatsService.getStatusStatistics(token);

        model.addAttribute("productStats", productStats);
        model.addAttribute("statusStats", statusStats);

        // Ajouter les stats quotidiennes
        PaymentDayStatistics dailyPaymentStats = paymentStatsService.getDailyPaymentStats(token);
        model.addAttribute("dailyPaymentStats", dailyPaymentStats);

        // Ajout des stats par source de paiement
        PaymentSourceStatistics paymentSourceStats = paymentStatsService.getPaymentSourceStats(token);
        model.addAttribute("paymentSourceStats", paymentSourceStats);

        //model.addAttribute("stats", offerService.getOffersStats(token));
        return "pages/dashboard/dashboard";
    }
}

