package s6.eval.first.crm.payment.stat.jour;

import java.util.List;

import lombok.Data;

@Data
public class PaymentDayDetail {
    private List<Payment> payments;
    private Integer total_amount;
    private String daily_total;
    

    public double getDailyTotalReel() {
        return Double.parseDouble(daily_total)/100;
    }

    @Data
    public static class Payment {
        private String external_id;
        private Double amount;
        private String payment_date;
        private Invoice invoice;

        public double getAmountReel() {
            return amount/100;
        }

        @Data
        public static class Invoice {
            private String external_id;
            private String status;
           /*  private Client client;
            
            @Data
            public static class Client {
                private Long id;
                private String company_name;
            } */
        }
    }
}
