package kz.pryahin.bitlabFinalProject.dtos.incomeDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateIncomeDto {
	@NotBlank
	private String name;

	private String description;

	@NotNull
	private BigDecimal amount;
	@NotNull
	private Long ledgerId;
	@NotNull
	private Long accountId;

	private String categoryName;

	private String subCategoryName;

}
