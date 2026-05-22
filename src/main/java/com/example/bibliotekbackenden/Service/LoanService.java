package com.example.bibliotekbackenden.Service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Entity.Loan;
import com.example.bibliotekbackenden.Exception.BookNotAvailableException;
import com.example.bibliotekbackenden.Exception.BookNotFoundException;
import com.example.bibliotekbackenden.Exception.LoanNotFoundException;
import com.example.bibliotekbackenden.Repository.BookRepository;
import com.example.bibliotekbackenden.Repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BookRepository bookRepository;

    /**
     * CRUD methods without updated.
     */
    @Transactional
    public Loan createLoan(Long bookId, Date loanDate, Date returnDate) {
        // Check if book already has an active loan (query within transaction)
        if (loanRepository.existsByBook_Id(bookId)) {
            throw new BookNotAvailableException(bookId);
        }
        // Attempt to create loan - if another thread modified Book, exception thrown
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException(bookId));

            Loan loan = new Loan();
            loan.setBook(book);
            loan.setBookTitle(book.getTitle());
            loan.setLoanDate(loanDate != null ? loanDate : new Date(System.currentTimeMillis()));
            loan.setReturnDate(returnDate);

            return loanRepository.saveAndFlush(loan);
        } catch (DataIntegrityViolationException e) {
            throw new BookNotAvailableException(bookId);
        }
    }

    public Loan getLoanById(Long id) {
        try {
            return loanRepository.findById(id).get();
        } catch (Exception e) {
            throw new LoanNotFoundException(id);
        }
    }

    public void deleteLoan(Long id) {

        try {
            Loan loan = getLoanById(id);
            loanRepository.delete(loan);
        } catch (Exception e) {
            throw new LoanNotFoundException(id);
        }
    }
}
