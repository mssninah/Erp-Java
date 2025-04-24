package s6.eval.first.crm.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentUpdateRequest {
    @NotNull
    @Min(0)
    private Double amount;
}
