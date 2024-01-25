package com.unir.loans.service;

import com.unir.loans.data.LoanJpaRepository;
import com.unir.loans.model.db.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.unir.loans.facade.BooksFacade;
import com.unir.loans.model.Book;
import com.unir.loans.model.request.LoanRequest;

@Service
public class LoansServiceImpl implements LoansService {

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private BooksFacade booksFacade;

  @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
  private LoanJpaRepository repository;

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
  }

  @Override
  public Loan getLoan(String id) {
    return repository.findById(Long.valueOf(id)).orElse(null);
  }

  @Override
  public List<Loan> getLoans() {
    List<Loan> loans = repository.findAll();
    return loans.isEmpty() ? null : loans;
  }
}
