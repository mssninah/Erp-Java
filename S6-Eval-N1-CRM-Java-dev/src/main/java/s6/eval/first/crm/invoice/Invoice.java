package s6.eval.first.crm.invoice;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Invoice {
    private Long id;

    @JsonProperty("external_id")
    private String externalId;

    private String status;
    private Integer invoiceNumber;

    
    @JsonProperty("total_price")
    private Double totalPrice;
    private String tauxremise; // Changé en String

    @JsonProperty("client_name")
    private String clientName;
    // ... autres champs
    // getters/setters

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}