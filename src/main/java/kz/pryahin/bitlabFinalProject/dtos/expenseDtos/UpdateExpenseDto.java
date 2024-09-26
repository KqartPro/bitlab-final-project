package kz.pryahin.bitlabFinalProject.dtos.expenseDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateExpenseDto {
	@NotNull
	private Long id;

	private String name;
	private String description;

	private BigDecimal amount;

	@NotNull
	private Long ledgerId;
	@NotNull
	private Long accountId;

	private String expenseCategoryName;

	private Long expenseSubCategoryId;

	private String expenseSubCategoryName;
}
