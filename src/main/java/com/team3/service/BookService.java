package com.team3.service;


import com.team3.entity.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    Book findById(Long id);

    Book remove(int id);

    List<Book> findAll(int pageNum, int pageCount);

    List<Book> findAll();

    List<Book> findBooksByTitle(String title);

    List<Book> findBooksByAuthor(int pageNum,int pageCount,String name);

    Long findCountOfBooksByAuthor(String name);

    List<Book> getMostPopularBooks(int pageNum, int pageCount);

    List<Book> getMostUnpopularBooks(int pageNum, int pageCount);
}
