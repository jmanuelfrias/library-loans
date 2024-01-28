package com.unir.loans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LoansApplication {


  @LoadBalanced
  @Bean
  public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
      return new RestTemplate(requestFactory);
  }

  public static void main(String[] args) {
    SpringApplication.run(LoansApplication.class, args);
  }

}
