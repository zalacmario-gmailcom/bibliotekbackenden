package com.example.bibliotekbackenden.Dto.Loan.v1;

import java.sql.Date;

public record LoanCreateDTO(
                Long bookId,
                Date loanDate,
                Date returnDate) {
}
