package com.unir.loans.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.loans.data.LoanRepository;
import com.unir.loans.model.Book;
import com.unir.loans.model.db.Loan;
import com.unir.loans.model.request.LoanRequest;
import com.unir.loans.model.request.RequestResult;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.unir.loans.facade.BooksFacade;

@Service
@Slf4j
public class LoansServiceImpl implements LoansService {

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private BooksFacade booksFacade;

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private LoanRepository repository;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public RequestResult createLoan(LoanRequest request) {
    RequestResult result = RequestResult.builder().created(null).result(400).build();
    Book foundBook = booksFacade.getBook(request.getBookId().toString());
    if(foundBook != null){
      LocalDate today = LocalDate.now();
      Loan loan = Loan.builder().user_id(request.getUserId()).book_id(foundBook.getId()).initial_date(Date.valueOf(today)).due_date(request.getDueDate()).build();

      //Necesario bajar el availability del libro que se toma prestado
      booksFacade.patchBook(request.getBookId().toString(), foundBook.getAvailability() - 1);
      //Salvar el Loan en base de datos y comprobar que no haya habido problema
      if(repository.save(loan)!=null) {
          result.setCreated(loan);
          result.setResult(201);
        } else {
          result.setResult(400);
        }
      }
    return result;
  }

  @Override
  public Loan getLoan(String id) {
    return repository.findById(Long.valueOf(id));
  }


  @Override
  public List<Loan> getLoans(Long user, Long book, Date minInitialDate, Date maxInitialDate,
                      Date mindueDate, Date maxdueDate, Date minEndDate, Date maxEndDate,Boolean returned){

    if ( user != null || book != null || minInitialDate != null || maxInitialDate != null || mindueDate != null ||
    maxdueDate != null || minEndDate != null || maxEndDate!= null || returned != null)
    {
      return repository.search(user,book,minInitialDate,maxInitialDate,mindueDate,maxdueDate,minEndDate,maxEndDate,returned);
    }
    List<Loan> loans = repository.getLoans();
    return loans.isEmpty() ? null : loans;
  }

  @Override
  public RequestResult updateLoan(String loanId, String updateRequest){
    RequestResult result = RequestResult.builder().created(null).result(400).build();
    Loan loan = repository.findById(Long.valueOf(loanId));
    if (loan != null) {
      try {
        String modifiedRequest = updateRequest.replace("[", "").replace("]", "");

        JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(modifiedRequest));
        JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(loan)));
        Loan patched = objectMapper.treeToValue(target, Loan.class);

        //Comprobar si el end_date que se quiere poner es anterior al día actual
        LocalDate today = LocalDate.now();
        LocalDate midnight = today.atStartOfDay().toLocalDate();
        Date todayMidnight = Date.valueOf(midnight);
        //Si es anterior, esta modificación no tiene sentido (asumimos que las devoluciones se registran en el día)
        if (!patched.getEnd_date().before(todayMidnight)){
          //Antes de modificar el loan, se va a comprobar que el libro siga en el catálogo
          Book foundBook = booksFacade.getBook(patched.getBook_id().toString());
          if (foundBook!=null){
            //Necesario subir el availability del libro que se toma prestado
            Book correctedBook = booksFacade.patchBook(patched.getBook_id().toString(), foundBook.getAvailability()+1);
            //Modificar el loan y comprobar si ha habido algún problema del JPA
            repository.returnBook(patched);
            result.setCreated(patched);
            result.setResult(200);
          } else {
            result.setResult(500);
          }

        }
      } catch (JsonProcessingException | JsonPatchException e) {
        log.error("Error updating loan {}. There was a problem with the input parameters", loanId);
      }
    } else {
      result.setResult(404);
    }
    return result;
}

}
