package com.team3.timer;

import com.team3.entity.Order;
import com.team3.entity.OrderStatus;
import com.team3.service.OrderService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
public class OrderTimer {

    private final long currentTime = System.currentTimeMillis();
    private final OrderService orderService;

    public OrderTimer(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void checkOrders() {
        checkOrdersIfBooksReturnedInTime();
        checkIfReservationDate();
    }

    public void checkOrdersIfBooksReturnedInTime() {
        for (Order order : orderService.findAllBorrowed()) {
            long deadline = order.getDeadline().getTime();
            if (order.getOrderStatus() == OrderStatus.BORROWED && currentTime > deadline) {
                order.setOrderStatus(OrderStatus.BANNED);
                orderService.updateOrder(order);
            }
        }
    }

    public void checkIfReservationDate() {
        for (Order order : orderService.findAllOrders()) {
            long reservationDeadline = addOneDay(order.getReserveDate()).getTime();

            if (currentTime > reservationDeadline
                    && order.getOrderStatus() == OrderStatus.RESERVED) {
                order.setOrderStatus(OrderStatus.CANCELED);
                orderService.updateOrder(order);
            }
        }
    }

    private Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 2);
        return new Date(calendar.getTimeInMillis());
    }
}