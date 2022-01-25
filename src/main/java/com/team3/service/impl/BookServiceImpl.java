package com.team3.service.impl;

import com.team3.dao.BookDao;
import com.team3.entity.Book;
import com.team3.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BookServiceImpl implements BookService {
    BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book save(Book book) {
        return bookDao.save(book);
    }

    @Override
    public Book findById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public Book remove(int id) {
        return bookDao.remove(id);
    }

    @Override
    public List<Book> findAll(int pageNum, int pageCount) {
        return bookDao.findAll(pageNum - 1, pageCount);
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookDao.findBooksByTitle(title);
    }

    @Override
    public List<Book> findBooksByAuthor(int pageNum, int pageCount, String name) {
        return bookDao.findBooksByAuthor(pageNum - 1, pageCount, name);
    }

    @Override
    public Long findCountOfBooksByAuthor(String name) {
        return bookDao.findCountOfBooksByAuthor(name);
    }

    @Override
    public List<Book> getMostPopularBooks(int pageNum, int pageCount) {
        return bookDao.getMostPopularBooks(pageNum - 1, pageCount);
    }

    @Override
    public List<Book> getMostUnpopularBooks(int pageNum, int pageCount) {
        return bookDao.getMostUnpopularBooks(pageNum - 1, pageCount);
    }
}