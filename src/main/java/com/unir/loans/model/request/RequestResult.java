package com.unir.loans.model.request;

import com.unir.loans.model.db.Loan;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestResult {
    public Loan created;
    public int result;
}
