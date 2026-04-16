package com.example.bibliotekbackenden.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public LoanResponseDTO getLoanById(Long id) {
        Loan loan = loanService.getLoanById(id);
        return new LoanResponseDTO(
                loan.getId(),
                loan.getBookId(),
                loan.getBookTitle(),
                loan.getLoanDate(),
                loan.getReturnDate());
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(Long id) {
        loanService.deleteLoan(id);
    }
}
