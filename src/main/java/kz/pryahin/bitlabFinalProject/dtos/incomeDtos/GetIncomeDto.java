package kz.pryahin.bitlabFinalProject.dtos.incomeDtos;

import kz.pryahin.bitlabFinalProject.entities.IncomeCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GetIncomeDto {
	private Long id;
	private String name;
	private String description;
	private BigDecimal amount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private IncomeCategory incomeCategory;
}
