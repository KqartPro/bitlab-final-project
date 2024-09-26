package kz.pryahin.bitlabFinalProject.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("My API").version("1.0").description("""
				<h2>Это приложение создано для контроля финансов и управления задачами.</h2>
				<h4>Для каждого запроса с замочком (который отображается с правого края) необходим Bearer токен который можно получить по эндпоинту auth/login. После получения токена нажмите на зеленую кнопку authorize и введите токен</h4>
				
				<p>Если вы захотите протестировать работу jwt и refresh jwt токенов, вы можете перейти в проекте по пути
				<code>kz.pryahin.bitlabFinalProject.security.services.impl.JwtServiceImpl</code> в методы
				<code>generateToken</code> и <code>generateRefreshToken</code>, и изменить значение переменной <strong>data</strong> - она отвечает за то, сколько
				времени токены валидны.
				По умолчанию <strong>Jwt token</strong> работает 1 час, а <strong>Refresh Jwt token</strong> работает 5 часов <br><br>
				
				Если же вы хотите проверить автоматическое удаление резервных юзеров, вы можете изменить поле <strong>date</strong> в <code>kz.pryahin.bitlabFinalProject.security.services.impl.UserServiceImpl</code> в методе <code>deleteUser</code> и изменить cron выражение в <code>kz.pryahin.bitlabFinalProject.security.services.impl.BackupUserServiceImpl</code> в методе <code>deleteExpiredBackupUsers</code>.
				По умолчанию юзеры с истекшей <strong>expiredDate</strong> удаляются через 30 дней в 00:00 часов</p>"""))
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("bearerAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization")));
	}
}

