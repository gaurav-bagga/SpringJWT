package com.green.jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.green.jwt.model.Book;
import com.green.jwt.service.BookService;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Simple controller performs CRUD on {@link Book} entity
 * 
 * @author gaurav.bagga
 *
 */
@Controller
@RequestMapping("/book")
public class BookController {

	@Autowired private BookService bookService;
	
	/**
	 * Finds book by specified id.
	 * 
	 * @param id
	 * @return Book instance JSON
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public @ResponseBody Book find(@PathVariable("id") Long id) {
		return bookService.find(id);
	}
}
