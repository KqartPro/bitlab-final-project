package kz.pryahin.bitlabFinalProject.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.entities.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
	void deleteAllByBackupUserId(Long id);
}
