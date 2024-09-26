package kz.pryahin.bitlabFinalProject.security.services;

public interface ValidateUserDataService {
	boolean isEmailValid(String email);

	boolean isPasswordValid(String password);

	String writePasswordRulesDescription();

	boolean isNameValid(String name);

	String writeNameRulesDescription();
}
