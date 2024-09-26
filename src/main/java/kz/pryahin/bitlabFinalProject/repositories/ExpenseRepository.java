package kz.pryahin.bitlabFinalProject.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
