package com.unir.loans.facade;

import com.unir.loans.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BooksFacade {

  @Value("${getBook.url}")
  private String getBookUrl;

  @Value("${patchBook.url}")
  private String patchBookUrl;

  private final RestTemplate restTemplate;

  public Book getBook(String id) {

    try {
      String url = String.format(getBookUrl, id);
      log.info("Getting book with ID {}. Request to {}", id, url);
      return restTemplate.getForObject(url, Book.class);
    } catch (HttpClientErrorException e) {
      log.error("Client Error: {}, Book with ID {}", e.getStatusCode(), id);
      return null;
    } catch (HttpServerErrorException e) {
      log.error("Server Error: {}, Book with ID {}", e.getStatusCode(), id);
      return null;
    } catch (Exception e) {
      log.error("Error: {}, Book with ID {}", e.getMessage(), id);
      return null;
    }
  }

  public Book patchBook(String id, int newAvailability) {
      String url = String.format(patchBookUrl, id);
      log.info("Modify availability of book with ID {} to {}. Request to {}", id, newAvailability, url);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      Map<String, Integer> availabilityMap = new HashMap<>();
      availabilityMap.put("availability", newAvailability);

      HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(availabilityMap, headers);

      ResponseEntity<Book> responseEntity = restTemplate.exchange(
              url,
              HttpMethod.PATCH,
              requestEntity,
              Book.class);

        return responseEntity.getBody();
  }

}
