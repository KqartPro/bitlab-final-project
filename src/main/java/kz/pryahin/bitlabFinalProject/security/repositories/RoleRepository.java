package kz.pryahin.bitlabFinalProject.security.repositories;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
}
