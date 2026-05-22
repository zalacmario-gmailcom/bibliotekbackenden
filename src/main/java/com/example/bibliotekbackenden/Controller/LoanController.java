package com.example.bibliotekbackenden.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.bibliotekbackenden.Dto.Loan.v1.LoanCreateDTO;
import com.example.bibliotekbackenden.Dto.Loan.v1.LoanResponseDTO;
import com.example.bibliotekbackenden.Entity.Loan;
import com.example.bibliotekbackenden.Service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/loans")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Loans", description = "Endpoints for managing loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    /**
     * CRUD operations for Loan entity.
     */
    @PostMapping
    @Operation(summary = "Create loan")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponseDTO createLoan(@Valid @RequestBody LoanCreateDTO dto) {
        Loan loan = loanService.createLoan(dto.bookId(), dto.loanDate(), dto.returnDate());

        return new LoanResponseDTO(
                loan.getId(),
                loan.getBookId(),
                loan.getBookTitle(),
                loan.getLoanDate(),
                loan.getReturnDate());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan by id")
    public LoanResponseDTO getLoanById(@PathVariable("id") Long id) {
        Loan loan = loanService.getLoanById(id);
        return new LoanResponseDTO(
                loan.getId(),
                loan.getBookId(),
                loan.getBookTitle(),
                loan.getLoanDate(),
                loan.getReturnDate());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete loan by id")
    public void deleteLoan(@PathVariable("id") Long id) {
        loanService.deleteLoan(id);
    }
}
