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

  @Autowired
  private BooksFacade booksFacade;

  @Autowired
  private LoanRepository repository;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public RequestResult createLoan(LoanRequest request) {
    RequestResult result = RequestResult.builder().created(null).result(400).build();
    Book foundBook = booksFacade.getBook(request.getBookId().toString());
    if(foundBook != null){
      LocalDate today = LocalDate.now();
      Loan loan = Loan.builder().userId(request.getUserId()).bookId(foundBook.getId()).initialDate(Date.valueOf(today)).dueDate(request.getDueDate()).build();

      //Necesario bajar el availability del libro que se toma prestado
      booksFacade.patchBook(request.getBookId().toString(), foundBook.getAvailability() - 1);
      //Salvar el Loan en base de datos y comprobar que no haya habido problema
      if(repository.save(loan)!=null) { //En este caso sí podemos salvarlo despues de modificar el catálogo porque se ha validado que está bien el request anteriormente y no puede dar problemas de tener el mismo id
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
        if (!patched.getEndDate().before(todayMidnight)){
          //Modificar el loan y comprobar si ha habido algún problema del JPA
          if (repository.returnBook(patched)!=null){
            //Si el patch ha funcionado, se devolverá un 200
            result.setCreated(patched);
            result.setResult(200);
            //Tras modificar el loan, se tienen que subir el availability del libro que se había cogido prestado
            try {
              Book foundBook = booksFacade.getBook(patched.getBookId().toString());
              Book correctedBook = booksFacade.patchBook(patched.getBookId().toString(), foundBook.getAvailability() + 1);
            } catch (Exception e){
              log.error("Problem updating the book");
            }
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
