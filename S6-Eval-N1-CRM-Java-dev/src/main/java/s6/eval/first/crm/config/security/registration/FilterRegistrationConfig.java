package s6.eval.first.crm.config.security.registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import s6.eval.first.crm.config.security.filter.JwtAuthFilter;


@Configuration
public class FilterRegistrationConfig {

    //@Value("${url.user}")
    //private String userURL;

    /* @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthFilter());
        registration.addUrlPatterns("/dashboard");
        registration.setOrder(1);
        return registration;
    }  */

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter(
        RestTemplate restTemplate, 
        ObjectMapper objectMapper
    ) {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthFilter(restTemplate, objectMapper));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
