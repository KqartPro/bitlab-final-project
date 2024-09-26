package kz.pryahin.bitlabFinalProject.dtos.accountDtos;

import jakarta.validation.constraints.NotNull;
import kz.pryahin.bitlabFinalProject.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountDto {
	@NotNull
	private Long id;

	@NotNull
	private Long ledgerId;

	private String name;

	private BigDecimal balance;

	private AccountType accountType;

}
