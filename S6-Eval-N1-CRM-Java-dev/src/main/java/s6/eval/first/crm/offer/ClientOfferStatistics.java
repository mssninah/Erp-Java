package s6.eval.first.crm.offer;

import lombok.Data;
import java.util.List;

@Data
public class ClientOfferStatistics {
    private List<ClientStatistic> statistics_by_client;
    private int total_offers;

    @Data
    public static class ClientStatistic {
        private String client_name;
        private int total_offers;
        private String percentage_of_total;
    }
}
