package com.team3.dao;

import com.team3.entity.Book;

import java.util.List;

public interface BookDao {

    Book save(Book book);

    Book findById(Long id);

    Book remove(int id);

    List<Book> findAll(int pageNum, int pageCount);

    List<Book> findBooksByTitle(String title);

    List<Book> findBooksByAuthor(String name);

    void updateBook(Book book);

    List<Book> getMostPopularBooks(int pageNum, int pageCount);

    List<Book> getMostUnpopularBooks(int pageNum, int pageCount);

    List<Book> findAll();
}