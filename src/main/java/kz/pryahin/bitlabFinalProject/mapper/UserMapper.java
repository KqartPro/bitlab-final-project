package kz.pryahin.bitlabFinalProject.mapper;

import kz.pryahin.bitlabFinalProject.security.dtos.GetUserDto;
import kz.pryahin.bitlabFinalProject.security.dtos.RegisterUserDto;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User mapRegisterUserDtoToUserEntity(RegisterUserDto registerUserDto);

	GetUserDto mapUserEntityToGetUserDto(User userEntity);

	BackupUser mapUserEntityToBackupUser(User userEntity);

	User mapBackupUserToUserEntity(BackupUser backupUser);
}
