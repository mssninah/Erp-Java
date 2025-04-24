package s6.eval.first.crm.invoice.stat.product;

import lombok.Data;

@Data
public class ProductQuantityStats {
    private Long id;
    private String name;
    private String total_quantity;
    private String percentage_of_total;

    // getters/setters
}
