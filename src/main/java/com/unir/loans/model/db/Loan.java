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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "initial_date")
    private Date initialDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "returned")
    private Boolean returned;
}
