package com.unir.loans.service;

import com.unir.loans.model.db.Loan;
import com.unir.loans.model.request.LoanRequest;
import com.unir.loans.model.request.RequestResult;

import java.sql.Date;
import java.util.List;

public interface LoansService {

	RequestResult createLoan(LoanRequest request);

	Loan getLoan(String id);

	List<Loan> getLoans(Long user, Long loan, Date minInitialDate, Date maxInitialDate,
						Date mindueDate, Date maxdueDate, Date minEndDate, Date maxEndDate,Boolean returned);

	Loan updateLoan(String loanId, String updateRequest);

}
