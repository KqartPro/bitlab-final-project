package kz.pryahin.bitlabFinalProject.dtos.accountDtos;

import kz.pryahin.bitlabFinalProject.entities.Expense;
import kz.pryahin.bitlabFinalProject.entities.Income;
import kz.pryahin.bitlabFinalProject.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GetAccountDto {
	private Long id;
	private String name;

	private BigDecimal balance;

	private AccountType accountType;

	private List<Expense> expenses;

	private List<Income> incomes;
}
