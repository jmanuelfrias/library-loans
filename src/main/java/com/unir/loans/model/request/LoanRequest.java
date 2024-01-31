package com.unir.loans.model.request;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

	@NotNull(message = "`userId` cannot be null")
	private Long userId;

	@NotNull(message = "`bookId` cannot be null")
	private Long bookId;

	@NotNull(message = "`dueDate` cannot be null")
	@Future(message = "The due date should be a date in the future")
	private Date dueDate;


}
