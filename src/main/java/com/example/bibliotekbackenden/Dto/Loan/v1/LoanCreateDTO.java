package com.example.bibliotekbackenden.Dto.Loan.v1;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;

public record LoanCreateDTO(
        @NotNull(message = "Book ID is required") Long bookId,
        Date loanDate,
        Date returnDate) {
}
