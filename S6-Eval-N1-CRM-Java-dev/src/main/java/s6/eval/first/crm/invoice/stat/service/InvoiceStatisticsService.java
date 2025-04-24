package s6.eval.first.crm.invoice.stat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import s6.eval.first.crm.invoice.stat.product.ProductStatistics;
import s6.eval.first.crm.invoice.stat.status.StatusStatistics;

@Service
public class InvoiceStatisticsService {

    @Value("${laravel.api.url}")
    private String apiUrl;

    public ProductStatistics getProductStatistics(String token) {
        return new RestTemplate().exchange(
            apiUrl + "/invoices/stat/product",
            HttpMethod.GET,
            createEntity(token),
            ProductStatistics.class
        ).getBody();
    }

    public StatusStatistics getStatusStatistics(String token) {
        return new RestTemplate().exchange(
            apiUrl + "/invoices/stat/status",
            HttpMethod.GET,
            createEntity(token),
            StatusStatistics.class
        ).getBody();
    }

    private HttpEntity<String> createEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}