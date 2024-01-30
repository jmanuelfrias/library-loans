package com.unir.loans.data;

import com.unir.loans.data.utils.SearchCriteria;
import com.unir.loans.data.utils.SearchOperation;
import com.unir.loans.data.utils.SearchStatement;
import com.unir.loans.model.db.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LoanRepository {

    private final LoanJpaRepository repository;

    public List<Loan> getLoans() {
        return repository.findAll();
    }

    public Loan findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Loan save(Loan loan) {
        loan.setReturned(false);
        return repository.save(loan);
    }

    public Loan returnBook(Loan loan) {
        return repository.save(loan);
    }

    public void delete(Loan loan) {
        repository.delete(loan);
    }


    public List<Loan> search(Long user, Long book, Date minInitialDate, Date maxInitialDate,
                             Date mindueDate, Date maxdueDate, Date minEndDate, Date maxEndDate, Boolean returned) {
        SearchCriteria<Loan> spec = new SearchCriteria<>();

        if (user != null) {
            spec.add(new SearchStatement("user_id", user, SearchOperation.EQUAL));
        }

        if (book != null) {
            spec.add(new SearchStatement("book_id", book, SearchOperation.EQUAL));
        }

        if (maxInitialDate != null) {
            spec.add(new SearchStatement("initial_date", maxInitialDate, SearchOperation.LESS_THAN));
        }

        if (minInitialDate != null) {
            spec.add(new SearchStatement("initial_date", (minInitialDate), SearchOperation.GREATER_THAN));
        }

        if (maxdueDate != null) {
            spec.add(new SearchStatement("due_date", (maxdueDate), SearchOperation.LESS_THAN));
        }

        if (mindueDate != null) {
            spec.add(new SearchStatement("due_date", (mindueDate), SearchOperation.GREATER_THAN));
        }

        if (maxEndDate != null) {
            spec.add(new SearchStatement("end_date", (maxEndDate), SearchOperation.LESS_THAN));
        }

        if (minEndDate != null) {
            spec.add(new SearchStatement("end_date", (minEndDate), SearchOperation.GREATER_THAN));
        }

        if (returned !=null){
            spec.add(new SearchStatement("returned", (returned), SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }

}
