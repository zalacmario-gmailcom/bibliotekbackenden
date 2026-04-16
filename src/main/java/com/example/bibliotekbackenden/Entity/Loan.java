package com.example.bibliotekbackenden.Entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "book_id", unique = true, nullable = false)
    private Book book;
    private String bookTitle;

    private Date loanDate = new Date(System.currentTimeMillis());
    private Date returnDate = null;

    public Loan() {
    }

    public Loan(Book book, String bookTitle, Date loanDate, Date returnDate) {
        this.book = book;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Long getBookId() {
        return book != null ? book.getId() : null;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
