package com.unir.loans.service;

import com.unir.loans.model.db.Loan;
import com.unir.loans.model.request.LoanRequest;
import java.util.List;

public interface LoansService {
	
	Loan createLoan(LoanRequest request);

	Loan getLoan(String id);

	List<Loan> getLoans();

}
