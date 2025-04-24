package s6.eval.first.crm.payment.stat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import s6.eval.first.crm.payment.stat.client.PaymentClientStatistics;
import s6.eval.first.crm.payment.stat.jour.PaymentDayDetail;
import s6.eval.first.crm.payment.stat.jour.PaymentDayStatistics;
import s6.eval.first.crm.payment.stat.source.PaymentSourceDetail;
import s6.eval.first.crm.payment.stat.source.PaymentSourceStatistics;

@Service
public class PaymentStatisticsService {

    @Value("${laravel.api.url}/payments/clients")
    private String clientsPaymentsUrl;

    private final RestTemplate restTemplate;

    public PaymentStatisticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentClientStatistics getClientPaymentStats(String token) {
        return getPaymentStats(clientsPaymentsUrl, token, PaymentClientStatistics.class);
    }

    private <T> T getPaymentStats(String url, String token, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType
            ).getBody();
        } catch (Exception e) {
            return null;
        }
    }


    @Value("${laravel.api.url}/payments/days")
    private String dailyPaymentsUrl;

    public PaymentDayStatistics getDailyPaymentStats(String token) {
        return getPaymentStats(dailyPaymentsUrl, token, PaymentDayStatistics.class);
    }

    public PaymentDayDetail getDailyPaymentsDetails(String date, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            return restTemplate.exchange(
                dailyPaymentsUrl + "/" + date,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PaymentDayDetail.class
            ).getBody();
        } catch (Exception e) {
            return null;
        }
    }


    @Value("${laravel.api.url}/payments/sources")
    private String paymentSourcesUrl;

    public PaymentSourceStatistics getPaymentSourceStats(String token) {
        return getPaymentStats(paymentSourcesUrl, token, PaymentSourceStatistics.class);
    }

    public PaymentSourceDetail getPaymentsBySource(String source, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            return restTemplate.exchange(
                paymentSourcesUrl + "/" + source,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PaymentSourceDetail.class
            ).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
