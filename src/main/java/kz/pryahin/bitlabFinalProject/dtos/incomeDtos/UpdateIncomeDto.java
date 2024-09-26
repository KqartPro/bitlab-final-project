package kz.pryahin.bitlabFinalProject.dtos.incomeDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateIncomeDto {
	@NotNull
	private Long id;

	private String name;
	private String description;

	private BigDecimal amount;

	@NotNull
	private Long ledgerId;
	@NotNull
	private Long accountId;

	private String incomeCategoryName;

	private Long incomeSubCategoryId;

	private String incomeSubCategoryName;
}
