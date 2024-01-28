package com.unir.loans.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {

	private Long id;
	private String title;
	private String author;
	private Integer yearPublication ;
	private String caratula;
	private Integer availability;
	private String sinopsis;
	private String genre;
	private Float puntuation;
	private String tags;
}
