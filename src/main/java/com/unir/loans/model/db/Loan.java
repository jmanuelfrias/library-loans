package com.unir.loans.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "loans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "bookId")
    private Long bookId;

    @Column(name = "initialDate")
    private Date initialDate;

    @Column(name = "loanedDate")
    private Date loanedDate;

    @Column(name = "endDate")
    private Date endDate;
}
