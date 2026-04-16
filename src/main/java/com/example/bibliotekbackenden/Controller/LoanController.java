package com.example.bibliotekbackenden.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.bibliotekbackenden.Dto.Loan.v1.LoanCreateDTO;
import com.example.bibliotekbackenden.Dto.Loan.v1.LoanResponseDTO;
import com.example.bibliotekbackenden.Entity.Loan;
import com.example.bibliotekbackenden.Service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * CRUD operations for Loan entity.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponseDTO createLoan(@RequestBody LoanCreateDTO dto) {
        Loan loan = loanService.createLoan(dto.bookId(), dto.loanDate(), dto.returnDate());

        return new LoanResponseDTO(
                loan.getId(),
                loan.getBookId(),
                loan.getBookTitle(),
                loan.getLoanDate(),
                loan.getReturnDate());
    }

    @GetMapping("/{id}")
    public LoanResponseDTO getLoanById(@PathVariable("id") Long id) {
        try {
            Loan loan = loanService.getLoanById(id);
            return new LoanResponseDTO(
                    loan.getId(),
                    loan.getBookId(),
                    loan.getBookTitle(),
                    loan.getLoanDate(),
                    loan.getReturnDate());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable("id") Long id) {
        try {
            loanService.deleteLoan(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found");
        }
    }
}
