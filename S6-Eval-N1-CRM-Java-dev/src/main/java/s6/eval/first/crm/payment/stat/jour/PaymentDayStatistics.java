package s6.eval.first.crm.payment.stat.jour;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Data;

@Data
public class PaymentDayStatistics {
    private List<DayPaymentStat> totals_by_day;
    private String total_amount;

    public double getTotalAmountReel() {
        return Double.parseDouble(total_amount)/100;
    }

    @Data
    public static class DayPaymentStat {
        private String payment_date;
        private String total_amount;
        private Integer number_of_payments;
        private String percentage_of_total;

        public double getTotalAmountReel() {
            return Double.parseDouble(total_amount)/100;
        }

        public String getFormattedDate() {
            return LocalDate.parse(payment_date.split("T")[0])
                          .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
}