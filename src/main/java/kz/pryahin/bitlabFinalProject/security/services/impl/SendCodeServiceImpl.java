package kz.pryahin.bitlabFinalProject.security.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kz.pryahin.bitlabFinalProject.security.services.SendCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendCodeServiceImpl implements SendCodeService {
	private final JavaMailSender javaMailSender;

	private void sendMessage(String email, String subject, String text) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(email); // User Email
			helper.setSubject(subject);
			helper.setText(text);

			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.getCause();
		}
	}

	@Override
	public void sendVerificationCode(String name, String surname, String verificationCode, String email) {
		String subject = "Your Verification Code";
		String text = String.format("Dear, %s %s. Thank you for registration. Your Verification code: %s", name, surname,
			verificationCode);
		sendMessage(email, subject, text);
	}

	@Override
	public void sendPasswordUpdateCode(String email, String codeToPasswordUpdate) {
		String subject = "Your update password code";
		String text = String.format("User %s has send the request to update password. Use this code to update your " +
			"password: %s", email, codeToPasswordUpdate);
		sendMessage(email, subject, text);
	}

	@Override
	public void resendVerificationCode(String email, String verificationCode) {
		String subject = "Your Verification Code";
		String text = String.format("Your Verification code: %s", verificationCode);
		sendMessage(email, subject, text);
	}

	@Override
	public void sendDeleteUserCode(String email, String deleteUserCode) {
		String subject = "Your delete account code";
		String text = String.format("User %s has sent the request to delete your account. Use this code to delete your " +
			"account: %s \n If you haven't sent this request, please don't show the code to anyone, and change your " +
			"password immediately", email, deleteUserCode);
		sendMessage(email, subject, text);
	}
}
