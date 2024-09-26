package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.CreateAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.GetAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.UpdateAccountDto;
import kz.pryahin.bitlabFinalProject.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
	private final AccountService accountService;

	@Operation(summary = "Возвращает все accounts в конкретном ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("get-all-accounts")
	public List<GetAccountDto> getAllAccounts(@RequestParam("ledgerId") Long ledgerId, Principal principal) {
		return accountService.getAllAccounts(ledgerId, principal);
	}

	@Operation(summary = "Возвращает конкретный account",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("get-account")
	public GetAccountDto getAccount(@RequestParam("ledgerId") Long ledgerId,
	                                @RequestParam("accountId") Long accountId,
	                                Principal principal) {
		return accountService.getAccount(ledgerId, accountId, principal);
	}

	@Operation(summary = "Создает новый account",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PostMapping("/create")
	public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto, Principal principal) {
		return accountService.createAccount(createAccountDto, principal);
	}

	@Operation(summary = "Обновляет одно или несколько полей конкретного account",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PatchMapping("/update")
	public ResponseEntity<?> updateAccount(@Valid @RequestBody UpdateAccountDto updateAccountDto, Principal principal) {
		return accountService.updateAccount(updateAccountDto, principal);
	}

	@Operation(summary = "Удаляет конкретный account",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete")
	public String deleteAccount(@RequestParam("ledgerId") Long ledgerId, @RequestParam("accountId") Long accountId,
	                            Principal principal) {
		return accountService.deleteAccount(ledgerId, accountId, principal);
	}
}
