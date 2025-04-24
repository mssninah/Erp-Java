package s6.eval.first.crm.invoice.stat.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductStatistics {
    private List<ProductQuantityStats> quantities_by_product;
    private String total_quantity;

    // getters/setters
}
