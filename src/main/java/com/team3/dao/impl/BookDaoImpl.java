package com.team3.dao.impl;

import com.team3.dao.BookDao;
import com.team3.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Transactional
public class BookDaoImpl implements BookDao {
    private final SessionFactory sessionFactory;
    private static final String BOOK_IDS_QUERY = "select b.bookId from Book  b order by b.bookId";

    @Autowired
    public BookDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.save(book);
        return book;
    }

    @Override
    public Book findById(Long id) {
        return sessionFactory.getCurrentSession().get(Book.class, id);
    }

    @Override
    public Book remove(int id) {
        return null;
    }

    @Override
    public List<Book> findAll(int pageNum, int pageCount) {
        String sQuery = "select b from Book  b left join fetch b.authors where b.bookId in (:bookIds) order by b.bookId asc ";
        return returnBookList(pageNum, pageCount, BOOK_IDS_QUERY, sQuery);
    }

    public List<Book> returnBookList(int pageNum, int pageCount, String idsQuery, String listQuery) {
        Session session = sessionFactory.getCurrentSession();
        List<Long> bookIds = session.createQuery(idsQuery, Long.class)
                .setFirstResult(pageNum * pageCount)
                .getResultList();
        bookIds = bookIds.stream().limit(pageCount).collect(Collectors.toList());
        String ids = bookIds.stream().map(Objects::toString).collect(Collectors.joining(","));
        try {
            return session.createQuery(listQuery, Book.class)
                    .setParameter("bookIds", bookIds)
                    .setParameter("ids", ids)
                    .setHint("hibernate.query.passDistinctThrough", false)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            return session.createQuery(listQuery, Book.class)
                    .setParameter("bookIds", bookIds)
                    .setHint("hibernate.query.passDistinctThrough", false)
                    .getResultList();
        }
    }


    @Override
    public List<Book> findBooksByTitle(String title) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Book where title like :title", Book.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    public List<Book> findBooksByAuthor(int pageNum, int pageCount, String name) {
        List<Long> bookIds = sessionFactory.getCurrentSession()
                .createQuery("SELECT b.bookId FROM Book b JOIN  BookAuthor pa ON pa.bookId = b.bookId JOIN  Author a ON a.id = pa.authorId WHERE a.surname LIKE :name OR a.name LIKE :name OR b.title LIKE :name", Long.class)
                .setParameter("name", name)
                .setFirstResult(pageNum * pageCount)
                .setMaxResults(pageCount)
                .getResultList();
        return sessionFactory.getCurrentSession()
                .createQuery("select b FROM Book b left join fetch b.authors where b.bookId in (:bookIds) order by b.bookId", Book.class)
                .setParameter("bookIds", bookIds)
                .getResultList();
    }

    @Override
    public Long findCountOfBooksByAuthor(String name) {
        return sessionFactory.getCurrentSession().createQuery("SELECT b FROM Book b JOIN  BookAuthor pa ON pa.bookId = b.bookId JOIN  Author a ON a.id = pa.authorId WHERE a.surname LIKE :name OR a.name LIKE :name OR b.title LIKE :name")
                .setParameter("name", name)
                .stream()
                .count();
    }


    @Override
    public void updateBook(Book book) {
        sessionFactory.getCurrentSession().update(book);
    }

    @Override
    public List<Book> getMostPopularBooks(int pageNum, int pageCount) {
        String fQuery = "select b.bookId from Book  b  left join Order o on o.book.bookId=b.bookId  group by b.bookId order by count (o.id) desc";
        String sQuery = "SELECT a from Book a left join fetch a.authors where a.bookId in (:bookIds) order by FIND_IN_SET(a.bookId, :ids)";
        return returnBookList(pageNum, pageCount, fQuery, sQuery);
    }

    @Override
    public List<Book> getMostUnpopularBooks(int pageNum, int pageCount) {
        String fQuery = "select b.bookId from Book  b  left join Order o on o.book.bookId=b.bookId  group by b.bookId order by count (o.id)";
        String sQuery = "SELECT a from Book a left join fetch a.authors where a.bookId in (:bookIds) order by FIND_IN_SET(a.bookId, :ids)";
        return returnBookList(pageNum, pageCount, fQuery, sQuery);
    }

    @Override
    public List<Book> findAll() {
        return sessionFactory.getCurrentSession().createQuery("select a from Book a", Book.class).getResultList();
    }
}