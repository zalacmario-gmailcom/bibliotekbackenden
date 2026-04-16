package com.example.bibliotekbackenden.Dto.Loan.v1;

import java.sql.Date;

public record LoanResponseDTO(
                Long id,
                Long bookId,
                String bookTitle,
                Date loanDate,
                Date returnDate) {
}
