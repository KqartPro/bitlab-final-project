package kz.pryahin.bitlabFinalProject.security.services.impl;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.repositories.LedgerRepository;
import kz.pryahin.bitlabFinalProject.repositories.TaskRepository;
import kz.pryahin.bitlabFinalProject.security.dtos.UserCredentialsDto;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;
import kz.pryahin.bitlabFinalProject.security.repositories.BackupUserRepository;
import kz.pryahin.bitlabFinalProject.security.services.BackupUserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackupUserServiceImpl implements BackupUserService {
	private static final Logger LOGGER = LogManager.getLogger(BackupUserServiceImpl.class);
	private final BackupUserRepository backupUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final LedgerRepository ledgerRepository;
	private final TaskRepository taskRepository;

	@Override
	public BackupUser getUserByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		Optional<BackupUser> backupUser = backupUserRepository.findByEmail(userCredentialsDto.getEmail());
		if (backupUser.isPresent()) {
			BackupUser user = backupUser.get();
			if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
				return user;
			}
		}
		throw new AuthenticationException("Email or password is not correct");
	}

//	@Scheduled(cron = " 0 */2 * * * ?") - Cron выражение для теста
	/* в это время из резервной таблицы удаляются expired юзеры */

	@Async
	@Transactional
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteExpiredBackupUsers() {

		LOGGER.info("Start");

		List<BackupUser> backupUsers = backupUserRepository.findAllByExpiredDateBeforeCurrent();

		if (!backupUsers.isEmpty()) {
			for (BackupUser backupUser : backupUsers) {
				taskRepository.deleteAllByBackupUserId(backupUser.getId());
				ledgerRepository.deleteAllByBackupUserId(backupUser.getId());
				backupUserRepository.delete(backupUser);
			}
			LOGGER.info("Deleted");
		}

		LOGGER.info("End");
	}


}
