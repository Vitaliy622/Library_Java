package com.team3.controllers;

import com.team3.entity.Book;
import com.team3.entity.Order;
import com.team3.entity.User;
import com.team3.service.BookService;
import com.team3.service.OrderService;
import com.team3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final OrderService orderService;
    private final UserService userService;
    private static final int BOOKS_IN_PAGE = 10;
    private static final String BOOKS_ATTRIBUTE = "books";
    private static final String ALL_BOOKS_PAGE = "book/bookAll";

    @Autowired
    public BookController(BookService bookService, OrderService orderService, UserService userService) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/show/page/{pageNum}")
    public String showAllBooks(@PathVariable int pageNum, Model model) {
        int totalResults = bookService.findAll().size();
        boolean empty = bookService.findAll(pageNum + 1, BOOKS_IN_PAGE).isEmpty();
        model.addAttribute(BOOKS_ATTRIBUTE, bookService.findAll(pageNum, BOOKS_IN_PAGE));
        pageElements(pageNum, model, empty, totalResults);
        return ALL_BOOKS_PAGE;
    }

    @GetMapping("/show/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "book/bookById";
    }

    @GetMapping("/show/search/{pageNum}")
    public ModelAndView showBooksByAuthorName(@PathVariable int pageNum,
                                              @RequestParam(required = false, value = "name") String name,
                                              Model model) {
        List<Book> list = bookService.findBooksByAuthor(pageNum, BOOKS_IN_PAGE, name);
        boolean empty = bookService.findBooksByAuthor(pageNum + 1, BOOKS_IN_PAGE, name).isEmpty();
        long totalResults = bookService.findCountOfBooksByAuthor(name);
        model.addAttribute("name", name);
        pageElements(pageNum, model, empty, totalResults);
        ModelAndView mav = new ModelAndView("book/bookByAuthor");
        mav.addObject("list", list);
        return mav;
    }

    @PostMapping("/show/{id}/reserveBook")
    public String reserveBooks(@PathVariable("id") Long id, Order order, Authentication authentication) {
        Book book = bookService.findById(id);
        User user = userService.getUserByEmail(authentication.getName());
        if (orderService.reserveBook(order, user, book)) {
            return "redirect:/books/successOrderCreation";
        } else {
            return "orders/failConfirm";
        }
    }

    @GetMapping("/show/{id}/reserveBook")
    public String reserveSuccess(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("order", new Order());
        return "orders/confirmation";
    }

    @GetMapping("/successOrderCreation")
    public String successOrder() {
        return "orders/reserveBook";
    }

    @GetMapping("/show/popular/{pageNum}")
    public String getMostPopularBooks(@PathVariable int pageNum, Model model) {
        int totalResults = bookService.findAll().size();
        boolean empty = bookService.getMostPopularBooks(pageNum + 1, BOOKS_IN_PAGE).isEmpty();
        model.addAttribute(BOOKS_ATTRIBUTE, bookService.getMostPopularBooks(pageNum, BOOKS_IN_PAGE));
        pageElements(pageNum, model, empty, totalResults);
        return ALL_BOOKS_PAGE;
    }

    @GetMapping("/show/unpopular/{pageNum}")
    public String getMostUnpopularBooks(@PathVariable int pageNum, Model model) {
        int totalResults = bookService.findAll().size();
        boolean empty = bookService.getMostUnpopularBooks(pageNum + 1, BOOKS_IN_PAGE).isEmpty();
        model.addAttribute(BOOKS_ATTRIBUTE, bookService.getMostUnpopularBooks(pageNum, BOOKS_IN_PAGE));
        pageElements(pageNum, model, empty, totalResults);
        return ALL_BOOKS_PAGE;
    }

    public void pageElements(int pageNum, Model model, boolean empty, long totalResults) {
        long totalPages = (totalResults + BOOKS_IN_PAGE - 1) / BOOKS_IN_PAGE;
        int next = pageNum + 1;
        int previous = pageNum - 1;
        model.addAttribute("isEmpty", empty);
        model.addAttribute("next", next);
        model.addAttribute("previous", previous);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNum);
    }
}