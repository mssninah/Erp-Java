package s6.eval.first.crm.offer;

import lombok.Data;
import java.util.List;

@Data
public class UserOfferStatistics {
    private List<UserStatistic> statistics_by_user;
    private int total_offers;

    @Data
    public static class UserStatistic {
        private String user_name;
        private int total_offers;
        private String percentage_of_total;
    }
}
