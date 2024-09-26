package kz.pryahin.bitlabFinalProject.dtos.ledgerDtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Currency;

@Data
public class UpdateLedgerDto {
	@NotNull
	private Long id;

	private String name;
	private Currency currency;
}
