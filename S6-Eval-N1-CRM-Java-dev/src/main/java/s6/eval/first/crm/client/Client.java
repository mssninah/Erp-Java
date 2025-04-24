package s6.eval.first.crm.client;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
    private Long id;
    
    @JsonProperty("external_id")
    private String externalId;
    
    private String address;
    private String zipcode;
    private String city;
    
    @JsonProperty("company_name")
    private String companyName;
    
    private String vat;
    
    @JsonProperty("company_type")
    private String companyType;
    
    @JsonProperty("client_number")
    private String clientNumber;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("industry_id")
    private Long industryId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    // ... Ajouter tous les getters/setters
}