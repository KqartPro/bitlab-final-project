package kz.pryahin.bitlabFinalProject.dtos.ledgerDtos;

import kz.pryahin.bitlabFinalProject.entities.Account;
import lombok.Data;

import java.util.Currency;
import java.util.List;

@Data
public class GetLedgerDto {
	private Long id;
	private String name;
	private Currency currency;
	private Long userId;
	private List<Account> accounts;
}
