package kz.pryahin.bitlabFinalProject.security.services;

import kz.pryahin.bitlabFinalProject.security.dtos.UserCredentialsDto;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;

import javax.naming.AuthenticationException;

public interface BackupUserService {
	BackupUser getUserByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException;
}
