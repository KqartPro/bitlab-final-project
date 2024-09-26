package kz.pryahin.bitlabFinalProject.dtos.expenseDtos;

import kz.pryahin.bitlabFinalProject.entities.ExpenseCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GetExpenseDto {
	private Long id;
	private String name;
	private String description;
	private BigDecimal amount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private ExpenseCategory expenseCategory;
}
