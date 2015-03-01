package com.green.jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.green.jwt.model.Book;

/**
 * Book service helps in CRUD operation on Book entity
 * 
 * @author gaurav.bagga
 *
 */
@Service
public class BookService {

	public static final Map<Long, Book> BOOK_DB = new HashMap<Long, Book>();
	
	static {
		BOOK_DB.put(1l, new Book(1l, "978-3-16-148410-0", "Java", "James Gosling", 1000));
		BOOK_DB.put(2l, new Book(2l, "978-3-16-148410-1", "Scala", "Martin Odersky", 1000));
		BOOK_DB.put(3l, new Book(3l, "978-3-16-148410-2", "Ruby", "Matz", 1000));
	}
	
	/**
	 * Finds book by id.
	 * 
	 * @param id
	 * @return
	 */
	public Book find(Long id){
		return BOOK_DB.get(id);
	}
	
	/**
	 * Saves the book into the database, but if id already present throws exception
	 * @param book
	 * @throws IllegalArgumentException if book already present in database.
	 */
	public void save(Book book){
		if(BOOK_DB.containsKey(book.id)){
			throw new IllegalArgumentException("Book already in database");
		}
		
		BOOK_DB.put(book.id, book);
	}
	
	/**
	 * Updates the book into the database
	 * @param book
	 */
	public void update(Book book){
		BOOK_DB.put(book.id, book);
	}
}
