package kz.pryahin.bitlabFinalProject.dtos.transactionDtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetTransactionStatisticsDto {
	private BigDecimal totalIncomesAmount;
	private BigDecimal totalExpensesAmount;
	private BigDecimal difference;
	private BigDecimal totalAccountsBalance;
}
