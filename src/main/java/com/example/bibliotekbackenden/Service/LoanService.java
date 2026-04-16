package com.example.bibliotekbackenden.Service;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Entity.Loan;
import com.example.bibliotekbackenden.Repository.BookRepository;
import com.example.bibliotekbackenden.Repository.LoanRepository;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * CRUD methods without updated.
     */
    public Loan createLoan(Long bookId, Date loanDate, Date returnDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Book not found or not available with id: " + bookId));

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBookTitle(book.getTitle());
        loan.setLoanDate(loanDate != null ? loanDate : new Date(System.currentTimeMillis()));
        loan.setReturnDate(returnDate);

        return loanRepository.save(loan);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found with id: " + id));
    }

    public Iterable<Loan> getAllLoans() {
        try {
            return loanRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No loans found");
        }
    }

    public void deleteLoan(Long id) {

        try {
            Loan loan = getLoanById(id);
            loanRepository.delete(loan);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found");
        }
    }
}
