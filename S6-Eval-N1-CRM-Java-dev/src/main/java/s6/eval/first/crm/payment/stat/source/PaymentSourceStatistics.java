package s6.eval.first.crm.payment.stat.source;

import java.util.List;

import lombok.Data;

@Data
public class PaymentSourceStatistics {
    private List<PaymentSourceStat> totals_by_source;
    private String total_amount;

    public double getTotalAmountReel() {
        return Double.parseDouble(total_amount)/100;
    }

    @Data
    public static class PaymentSourceStat {
        private String payment_source;
        private Integer total_payments;
        private String total_amount;
        private String percentage_of_total;

        public double getTotalAmountReel() {
            return Double.parseDouble(total_amount)/100;
        }
    }
}
