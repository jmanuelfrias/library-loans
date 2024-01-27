package com.unir.loans.service;

import com.unir.loans.model.db.Loan;

import java.sql.Date;
import java.util.List;

public interface LoansService {
	
	//Loan createLoan(LoanRequest request);

	Loan getLoan(String id);

	List<Loan> getLoans(Long user, Long book, Date minInitialDate, Date maxInitialDate,
						Date minLoanedDate, Date maxLoanedDate, Date minEndDate, Date maxEndDate);

}
