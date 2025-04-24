package s6.eval.first.crm.invoice;

import java.util.List;

import lombok.Data;

@Data
public class InvoiceListResponse {
    private List<Invoice> invoices;
    private int total;

    // getters/setters
}
