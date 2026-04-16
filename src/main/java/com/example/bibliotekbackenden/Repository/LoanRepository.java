package com.example.bibliotekbackenden.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bibliotekbackenden.Entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
