package kz.pryahin.bitlabFinalProject.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface IncomeRepository extends JpaRepository<Income, Long> {

}
