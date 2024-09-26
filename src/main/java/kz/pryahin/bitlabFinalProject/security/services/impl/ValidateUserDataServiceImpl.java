package kz.pryahin.bitlabFinalProject.security.services.impl;

import kz.pryahin.bitlabFinalProject.security.services.ValidateUserDataService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateUserDataServiceImpl implements ValidateUserDataService {

	private static final String REGEX_EMAIL = "^[a-z0-9A-Z._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	private static final String REGEX_NAME = "^[A-Za-zА-Яа-яёЁ]{2,30}([A-Za-zА-Яа-яёЁ]{2,30})?$";
	private static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$";

	@Override
	public boolean isEmailValid(String email) {
		Pattern pattern = Pattern.compile(REGEX_EMAIL, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public boolean isNameValid(String name) {
		Pattern pattern = Pattern.compile(REGEX_NAME, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	@Override
	public String writeNameRulesDescription() {
		return """
			Incorrect name or surname. Requirements:
			Length: 2 to 30 characters.
			Only letters
			""";
	}

	@Override
	public boolean isPasswordValid(String password) {
		Pattern pattern = Pattern.compile(REGEX_PASSWORD);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	@Override
	public String writePasswordRulesDescription() {
		return """
			Incorrect password. Password requirements:
			Length: 8 to 20 characters.
			Must contain at least:
			One number.
			One lowercase letter.
			One capital letter.
			One special character from the set: @#$%^&+=!""";
	}

}
