package s6.eval.first.crm.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Payment {
    @JsonProperty("external_id") // Annotation cruciale
    private String externalId;
    
    private Integer amount;
    
    @JsonProperty("payment_source")
    private String paymentSource;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("payment_date")
    private LocalDateTime paymentDate;
    
    @JsonProperty("invoice_id")
    private Long invoiceId;

    public double getAmountReel() {
        return (double) amount/100;
    }
}