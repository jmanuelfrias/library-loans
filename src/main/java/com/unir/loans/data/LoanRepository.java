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
        return repository.save(loan);
    }

    public void delete(Loan loan) {
        repository.delete(loan);
    }


    public List<Loan> search(Long user, Long book, Date minInitialDate, Date maxInitialDate,
                             Date minLoanedDate, Date maxLoanedDate, Date minEndDate, Date maxEndDate) {
        SearchCriteria<Loan> spec = new SearchCriteria<>();

        if (user != null) {
            spec.add(new SearchStatement("user_id", user, SearchOperation.EQUAL));
        }

        if (book != null) {
            spec.add(new SearchStatement("book_id", book, SearchOperation.EQUAL));
        }

        List<Loan> foundLoans = repository.findAll(spec);



        if (maxInitialDate != null) {
            spec.add(new SearchStatement("initial_date", maxInitialDate, SearchOperation.LESS_THAN));
        }

        if (minInitialDate != null) {
            spec.add(new SearchStatement("initial_date", (minInitialDate), SearchOperation.GREATER_THAN));
        }

        if (maxLoanedDate != null) {
            spec.add(new SearchStatement("loaned_date", (maxLoanedDate), SearchOperation.LESS_THAN));
        }

        if (minLoanedDate != null) {
            spec.add(new SearchStatement("loaned_date", (minLoanedDate), SearchOperation.GREATER_THAN));
        }

        if (maxEndDate != null) {
            spec.add(new SearchStatement("end_date", (maxEndDate), SearchOperation.LESS_THAN));
        }

        if (minEndDate != null) {
            spec.add(new SearchStatement("end_date", (minEndDate), SearchOperation.GREATER_THAN));
        }

        return repository.findAll(spec);
    }

}
