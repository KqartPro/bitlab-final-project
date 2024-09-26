package kz.pryahin.bitlabFinalProject.dtos.categoryDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteExpenseSubCategoryDto {
	@NotNull
	private Long id;
	@NotNull
	private Long ledgerId;
	@NotNull
	private Long accountId;
	@NotNull
	private Long expenseId;
}
