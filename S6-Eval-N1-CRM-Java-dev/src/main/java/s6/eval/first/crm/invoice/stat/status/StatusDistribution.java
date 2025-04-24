package s6.eval.first.crm.invoice.stat.status;

import java.util.List;

import lombok.Data;

@Data
public class StatusDistribution {
    private String status;
    private Integer total;
    private String percentage;
    private String paid_count;
    private String paid_percentage;
    private Number total_price;
    private List<Object> invoice_lines;

    // getters/setters
}