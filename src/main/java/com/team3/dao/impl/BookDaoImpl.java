package com.team3.dao.impl;

import com.team3.dao.BookDao;
import com.team3.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BookDaoImpl implements BookDao {
    private final SessionFactory sessionFactory;

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
        String fQuery = "select b.bookId from Book  b order by b.bookId";
        String sQuery = "select distinct b from Book  b left join fetch b.authors where b.bookId in (:bookIds) order by b.bookId asc ";
        return returnBookList(pageNum, pageCount, fQuery, sQuery);
    }

    public List<Book> returnBookList(int pageNum, int pageCount, String idsQuery, String listQuery) {
        Session session = sessionFactory.getCurrentSession();
        List<Long> bookIds = session.createQuery(idsQuery, Long.class)
                .setFirstResult(pageNum * pageCount)
                .setMaxResults(pageCount)
                .getResultList();
        return session.createQuery(listQuery, Book.class)
                .setParameter("bookIds", bookIds)
                .setHint("hibernate.query.passDistinctThrough", false)
                .getResultList();
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Book where title like :title", Book.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    public List<Book> findBooksByAuthor(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT b\n" +
                        "FROM Book b\n" +
                        "          JOIN fetch b.authors" +
                        "          JOIN fetch BookAuthor pa ON pa.bookId = b.bookId\n" +
                        "          JOIN fetch Author a ON a.id = pa.authorId\n" +
                        "WHERE a.surname LIKE :name\n" +
                        "   OR a.name LIKE :name " +
                        "   OR b.title LIKE :name", Book.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public void updateBook(Book book) {
        sessionFactory.getCurrentSession().update(book);
    }
}