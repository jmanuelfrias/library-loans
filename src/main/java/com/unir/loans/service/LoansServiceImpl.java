package com.unir.loans.service;

import com.unir.loans.data.LoanJpaRepository;
import com.unir.loans.data.LoanRepository;
import com.unir.loans.model.db.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

import com.unir.loans.facade.BooksFacade;

@Service
public class LoansServiceImpl implements LoansService {

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private BooksFacade booksFacade;

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private LoanRepository repository;
/*
  @Override
  public Loan createLoan(LoanRequest request) {

    List<Book> books = request.getBooks().stream().map(booksFacade::getBook).filter(Objects::nonNull).toList();

    if(books.size() != request.getBooks().size() || books.stream().anyMatch(book -> !book.getVisible())) {
      return null;
    } else {
      Loan loan = Loan.builder().books(books.stream().map(Book::getId).collect(Collectors.toList())).build();
      repository.save(loan);
      return loan;
    }
  }*/

  @Override
  public Loan getLoan(String id) {
    return repository.findById(Long.valueOf(id));
  }


  @Override
  public List<Loan> getLoans(Long user, Long book, Date minInitialDate, Date maxInitialDate,
                      Date minLoanedDate, Date maxLoanedDate, Date minEndDate, Date maxEndDate,Boolean returned){

    if ( user != null || book != null || minInitialDate != null || maxInitialDate != null || minLoanedDate != null ||
    maxLoanedDate != null || minEndDate != null || maxEndDate!= null || returned != null)
    {
      return repository.search(user,book,minInitialDate,maxInitialDate,minLoanedDate,maxLoanedDate,minEndDate,maxEndDate,returned);
    }
    List<Loan> loans = repository.getLoans();
    return loans.isEmpty() ? null : loans;


  }

}
