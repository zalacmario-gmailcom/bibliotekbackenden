package com.example.bibliotekbackenden.Service;

import java.sql.Date;
import org.springframework.stereotype.Service;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Entity.Loan;
import com.example.bibliotekbackenden.Exception.AuthorNotFoundException;
import com.example.bibliotekbackenden.Exception.BookNotAvailableException;
import com.example.bibliotekbackenden.Exception.LoanNotFoundException;
import com.example.bibliotekbackenden.Repository.BookRepository;
import com.example.bibliotekbackenden.Repository.LoanRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;

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
    @Transactional
    public Loan createLoan(Long bookId, Date loanDate, Date returnDate) {
        // Check if book already has an active loan (query within transaction)
        if (loanRepository.findById(bookId).isPresent()) {
            throw new BookNotAvailableException(bookId);
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AuthorNotFoundException(bookId));

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBookTitle(book.getTitle());
        loan.setLoanDate(loanDate != null ? loanDate : new Date(System.currentTimeMillis()));
        loan.setReturnDate(returnDate);

        // Attempt to create loan - if another thread modified Book, exception thrown
        try {
            return loanRepository.save(loan);
        } catch (OptimisticLockException e) {
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
