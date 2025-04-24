package s6.eval.first.crm.payment.stat.client;

import lombok.Data;
import java.util.List;

@Data
public class PaymentClientStatistics {
    private List<ClientPaymentStat> totals_by_client;
    private String total_amount;

    @Data
    public static class ClientPaymentStat {
        private Long id;
        private String company_name;
        private String total_amount;
        private Integer number_of_payments;
        private String percentage_of_total;

        public double getTotalAmountReel() {
            return Double.parseDouble(total_amount)/100;
        }
    }

    public double getTotalAmountReel() {
        return Double.parseDouble(total_amount)/100;
    }
}
