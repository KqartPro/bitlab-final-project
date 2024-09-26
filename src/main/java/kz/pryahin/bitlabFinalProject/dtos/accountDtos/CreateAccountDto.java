package kz.pryahin.bitlabFinalProject.dtos.accountDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.pryahin.bitlabFinalProject.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {
	@NotNull
	private Long ledgerId;
	@NotBlank
	private String name;

	private BigDecimal balance;

	private AccountType accountType;

}
