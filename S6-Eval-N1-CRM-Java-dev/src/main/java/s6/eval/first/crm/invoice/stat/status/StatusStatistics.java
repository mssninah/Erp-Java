package s6.eval.first.crm.invoice.stat.status;

import java.util.List;

import lombok.Data;

@Data
public class StatusStatistics {
    private List<StatusDistribution> status_distribution;
    private Integer total_invoices;

    // getters/setters
}