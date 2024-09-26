package kz.pryahin.bitlabFinalProject.security.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BackupUserRepository extends JpaRepository<BackupUser, Long> {
	@Modifying
	@Query("SELECT backupUser FROM BackupUser backupUser WHERE backupUser.expiredDate < CURRENT_TIMESTAMP")
	List<BackupUser> findAllByExpiredDateBeforeCurrent();

	Optional<BackupUser> findByEmail(String email);

	void deleteByEmail(String email);


}
