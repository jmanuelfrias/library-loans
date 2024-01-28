package com.unir.loans.service;

import com.unir.loans.data.LoanJpaRepository;
import com.unir.loans.data.LoanRepository;
import com.unir.loans.model.Book;
import com.unir.loans.model.db.Loan;
import com.unir.loans.model.request.LoanRequest;
import com.unir.loans.model.request.RequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.unir.loans.facade.BooksFacade;

@Service
public class LoansServiceImpl implements LoansService {

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private BooksFacade booksFacade;

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private LoanRepository repository;

  @Override
  public RequestResult createLoan(LoanRequest request) {
    RequestResult result = RequestResult.builder().created(null).result(400).build();
    Book foundBook =booksFacade.getBook(request.getBookId().toString());
    if(foundBook == null){
      result.setResult(404);
    } else if (foundBook.getAvailability() < 1) {
      result.setResult(409);
    } else {
      LocalDate today = LocalDate.now();
      Loan loan = Loan.builder().user_id(request.getUserId()).book_id(foundBook.getId()).initial_date(Date.valueOf(today)).due_date(request.getDueDate()).build();
      repository.save(loan);
      result.setCreated(loan);
      result.setResult(200);
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


}
