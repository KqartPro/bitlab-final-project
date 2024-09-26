package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.CreateLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.GetLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.UpdateLedgerDto;
import kz.pryahin.bitlabFinalProject.services.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ledger")
public class LedgerController {
	private final LedgerService ledgerService;

	@Operation(summary = "Возвращает все ledgers пользователя (ledger хранит аккаунты, транзакции и категории)",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/get-all-ledgers")
	public List<GetLedgerDto> getAllLedgers(Principal principal) {
		return ledgerService.getAllLedgers(principal);
	}

	@Operation(summary = "Возвращает конкретный ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/get-ledger")
	public GetLedgerDto getLedger(@RequestParam("ledgerId") Long ledgerId, Principal principal) {
		return ledgerService.getLedger(ledgerId, principal);
	}

	@Operation(summary = "Создает ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PostMapping("/create")
	public ResponseEntity<?> createLedger(@Valid @RequestBody CreateLedgerDto createLedgerDto, Principal principal) {
		return ledgerService.createLedger(createLedgerDto, principal);
	}

	@Operation(summary = "Обновляет один или несколько введенных параметров ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PatchMapping("/update")
	public ResponseEntity<?> updateLedger(@Valid @RequestBody UpdateLedgerDto updateLedgerDto, Principal principal) {
		return ledgerService.updateLedger(updateLedgerDto, principal);
	}

	@Operation(summary = "Удаляет конкретный ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("delete")
	public String deleteLedger(@RequestParam("ledgerId") Long ledgerId, Principal principal) {
		return ledgerService.deleteLedger(ledgerId, principal);
	}

	@Operation(summary = "Возвращает статистику по аккаунтам, транзакциям и категориям которые принадлежат " +
		"конкретному ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("get-all-statistics")
	public ResponseEntity<?> getAllStatistics(@RequestParam("ledgerId") Long ledgerId, Principal principal) {
		return ledgerService.getAllStatistics(ledgerId, principal);
	}
}
