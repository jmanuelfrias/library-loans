package com.unir.loans.model.db;

import com.unir.loans.model.Book;
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
    private Long user_id;

    @Column(name = "book_id")
    private Long book_id;

    @Column(name = "initial_date")
    private Date initial_date;

    @Column(name = "due_date")
    private Date due_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "returned")
    private Boolean returned;
}
