package s6.eval.first.crm.payment;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class PaymentDetails {
    private List<Payment> payments;
    private Integer total_amount;
    private String client_total;

    @Data
    public static class Payment {
        private String external_id;
        private Integer amount;
        private String payment_source;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private LocalDateTime payment_date;
        
        private Invoice invoice;

        public double getAmountReel() {
            return (double) amount/100;
        }
    }

    @Data
    public static class Invoice {
        private String external_id;
        private String status;
        private Integer total_price;
        private List<InvoiceLine> invoice_lines;
    }

    @Data
    public static class InvoiceLine {
        private String title;
        private Integer price;
        private Integer quantity;
        private String type;
    }
}

