package kz.pryahin.bitlabFinalProject.security.services;

public interface SendCodeService {
	void sendVerificationCode(String name, String surname, String verificationCode, String email);

	void sendPasswordUpdateCode(String email, String codeToPasswordUpdate);

	void resendVerificationCode(String email, String verificationCode);

	void sendDeleteUserCode(String email, String deleteUserCode);
}
