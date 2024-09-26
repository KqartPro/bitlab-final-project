package kz.pryahin.bitlabFinalProject.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com"); // Укажите ваш SMTP сервер
		mailSender.setPort(587); // Укажите порт, используемый вашим SMTP сервером

		mailSender.setUsername("uniprojassist@gmail.com"); // Укажите имя пользователя
		mailSender.setPassword("ajciwsydyaznrfvc"); // Укажите пароль (Созданный для использования вашей почты другими сервисами, не основной пароль от почты)

		// Дополнительные настройки, если необходимо
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		return mailSender;
	}
}
