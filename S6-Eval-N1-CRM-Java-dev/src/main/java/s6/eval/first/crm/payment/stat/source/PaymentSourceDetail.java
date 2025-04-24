package s6.eval.first.crm.payment.stat.source;

import java.util.List;

import lombok.Data;

@Data
public class PaymentSourceDetail {
    private List<Payment> payments;
    private Integer total;

    public double getTotalReel() {
        return Double.parseDouble(total+"")/100;
    }

    @Data
    public static class Payment {
        private String external_id;
        private Double amount;
        private String payment_source;
        private Invoice invoice;

        public double getAmountReel() {
            return amount/100;
        }
        
        @Data
        public static class Invoice {
            private String external_id;
            /* private Client client;
            
            @Data
            public static class Client {
                private String company_name;
            } */
        }
    }
}