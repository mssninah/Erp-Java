package s6.eval.first.crm.offer;

import lombok.Data;
import java.util.List;

@Data
public class OfferStatistics {
    private List<Statistic> statistics;
    private int total_offers;

    @Data
    public static class Statistic {
        private String status;
        private int total;
        private String percentage;
    }
}
