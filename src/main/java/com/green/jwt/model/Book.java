package com.green.jwt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity model representing Book in the system.
 * 
 * @author gaurav.bagga
 *
 */
public class Book {

	public final Long id;
	public final String isbn;
	public final String title;
	public final String author;
	public final Integer cost;//cents
	
	@JsonCreator
	public Book(@JsonProperty("id") Long id, 
			@JsonProperty("isbn") String isbn, 
			@JsonProperty("title") String title, 
			@JsonProperty("author") String author, 
			@JsonProperty("cost") Integer cost) {
		super();
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.cost = cost;
	}
	
	
	
}
