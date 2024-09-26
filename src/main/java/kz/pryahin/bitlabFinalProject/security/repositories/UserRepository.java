package kz.pryahin.bitlabFinalProject.security.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	void deleteByEmail(String email);

}
