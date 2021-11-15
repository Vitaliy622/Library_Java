package com.team3.service.impl;

import com.team3.dao.BookDao;
import com.team3.dao.OrderDao;
import com.team3.entity.Book;
import com.team3.entity.Order;
import com.team3.entity.OrderStatus;
import com.team3.entity.User;
import com.team3.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final BookDao bookDao;

    private final long millis = System.currentTimeMillis();
    private final Date date = new Date(millis);


    @Autowired
    public OrderServiceImpl(OrderDao orderDao, BookDao bookDao) {
        this.orderDao = orderDao;
        this.bookDao = bookDao;
    }

    //TODO: add implementation to methods
    @Override
    public void getTheMostPopularBook() {
    }

    @Override
    public void getTheMostUnpopularBook() {
    }

    @Override
    @Transactional
    public int getHowManyBooksWereBeenReadByUser(String email) {
        return orderDao.getHowManyBooksWereBeenReadByUser(email).size();
    }

    @Override
    public void getHowManyBooksWereGivingInSelectedPeriod() {
    }

    @Override
    @Transactional
    public boolean reserveBook(Order order, User user, Book book) {
        int repeatedOrders = orderDao.getUsersRepeatedOrders(user.getId(), book.getBookId()).size();
        if (repeatedOrders >= 1) {
            return false;
        } else {
            order.setUser(user);
            order.setBook(book);
            order.setReserveDate(date);
            order.setOrderStatus(OrderStatus.RESERVED);
            book.setCount(book.getCount() - 1);
            bookDao.updateBook(book);
            orderDao.addOrder(order);
            return true;
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderDao.deleteOrder(id);
    }

    @Override
    @Transactional
    public Order findByOrderId(Long id) {
        return orderDao.findOrderById(id);
    }

    @Override
    @Transactional
    public List<Order> findAllOrders() {
        return orderDao.findAllOrders();
    }

    @Override
    @Transactional
    public void borrowBook(Long id) {
        Date borrowDate = new Date(millis);
        Order order = orderDao.findOrderById(id);
        order.setTakeBook(borrowDate);

        Date deadlineDate = addOneMonth(borrowDate);
        order.setDeadline(deadlineDate);

        order.setOrderStatus(OrderStatus.BORROWED);
        orderDao.addOrder(order);
    }

    @Override
    @Transactional
    public void returnBook(Long id) {
        Date returnDate = new Date(millis);
        Order order = orderDao.findOrderById(id);

        order.setReturnBook(returnDate);
        order.setReturned(true);
        order.setOrderStatus(OrderStatus.CLOSED);
        orderDao.addOrder(order);
    }

    private Date addOneMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return new Date(calendar.getTimeInMillis());
    }

    @Override
    @Transactional
    public int getAverageReadingTimeOfUser(String user) {
        List<Order> orders = orderDao.getHowManyBooksWereBeenReadByUser(user);
        int time = 0;
        int toHour = 3600000;
        for (Order order : orders) {
            time += millis - order.getTakeBook().getTime();
        }
        time = time / toHour;
        return time;
    }

    @Override
    @Transactional
    public List<String> getBooksThatUserReading(String email) {
        List<Order> orders = orderDao.getHowManyBooksWereBeenReadByUser(email);
        List<String> books = new ArrayList<>();
        for (Order o : orders) {
            books.add(o.getBook().getTitle());
        }
        return books;
    }
}