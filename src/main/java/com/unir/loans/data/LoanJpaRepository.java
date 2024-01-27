package com.unir.loans.data;

import com.unir.loans.model.db.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Date;
import java.util.List;

public interface LoanJpaRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    List<Loan> findByBookId(Long book);

    List<Loan> findByUserId(Long user);

    List<Loan> findByInitialDateBefore(Date initialDate);

    List<Loan> findByInitialDateAfter(Date initialDate);

    List<Loan> findByLoanedDateBefore(Date loanedDate);

    List<Loan> findByLoanedDateAfter(Date loanedDate);

    List<Loan> findByEndDateBefore(Date endDate);

    List<Loan> findByEndDateAfter(Date endDate);



}
