package kz.pryahin.bitlabFinalProject.dtos.ledgerDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Currency;

@Data
public class CreateLedgerDto {
	@NotBlank
	private String name;
	@NotNull
	private Currency currency;

}
