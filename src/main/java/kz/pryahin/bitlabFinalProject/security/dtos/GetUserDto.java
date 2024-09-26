package kz.pryahin.bitlabFinalProject.security.dtos;

import lombok.Data;

@Data
public class GetUserDto {
	private String name;
	private String surname;
	private String email;
	private boolean verificationStatus;
}
