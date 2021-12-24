package com.team3.service;

import com.team3.entity.Book;
import com.team3.entity.Order;
import com.team3.entity.User;

import java.math.BigInteger;
import java.util.List;

public interface OrderService {
    List<Book> getTheMostPopularBook();

    List<Book> getTheMostUnpopularBook();

    int getHowManyBooksWereBeenReadByUser(String email);

    void getHowManyBooksWereGivingInSelectedPeriod();

    boolean reserveBook(Order order, User user, Book book);

    void deleteOrder(Long id);

    Order findByOrderId(Long id);

    List<Order> findAllOrders();

    List<Order> findAllBorrowed();

    void borrowBook(Long id);

    BigInteger getAverageReadingTimeOfUser(String user);

    String getBooksThatUserReading(String email);

    void returnBook(Long id);

    void updateOrder(Order order);

}