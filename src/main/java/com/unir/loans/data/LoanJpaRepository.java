package com.unir.loans.data;

import com.unir.loans.model.db.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Date;
import java.util.List;

public interface LoanJpaRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {


}
