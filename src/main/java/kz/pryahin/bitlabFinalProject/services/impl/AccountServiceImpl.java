package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.dtos.accountDtos.CreateAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.GetAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.UpdateAccountDto;
import kz.pryahin.bitlabFinalProject.entities.Account;
import kz.pryahin.bitlabFinalProject.entities.Ledger;
import kz.pryahin.bitlabFinalProject.exceptions.AccountNotFoundException;
import kz.pryahin.bitlabFinalProject.exceptions.LedgerNotFoundException;
import kz.pryahin.bitlabFinalProject.mapper.FinanceMapper;
import kz.pryahin.bitlabFinalProject.repositories.AccountRepository;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import kz.pryahin.bitlabFinalProject.services.AccountService;
import kz.pryahin.bitlabFinalProject.services.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final FinanceMapper financeMapper;
	private final UserService userService;
	private final AccountRepository accountRepository;
	private final LedgerService ledgerService;

	@Override
	public Account getAccountEntity(Long ledgerId, Long accountId, Principal principal) {
		Ledger ledger = ledgerService.getLedgerEntity(ledgerId, principal);

		return ledger.getAccounts().stream()
			.filter(a -> a.getId().equals(accountId))
			.findFirst()
			.orElseThrow(AccountNotFoundException::new);
	}

	@Override
	public GetAccountDto getAccount(Long ledgerId, Long accountId, Principal principal) {
		return financeMapper.mapAccountToGetAccountDto(getAccountEntity(ledgerId, accountId, principal));
	}

	@Override
	public List<GetAccountDto> getAllAccounts(Long ledgerId, Principal principal) {
		return financeMapper.mapAccountsListToGetAccountsListDto(ledgerService.getLedgerEntity(ledgerId, principal).getAccounts());
	}

	@Override
	public ResponseEntity<?> createAccount(CreateAccountDto createAccountDto, Principal principal) {
		User user = userService.getUserEntity(principal.getName());

		Ledger ledger = user.getLedgers().stream().
			filter(l -> l.getId().equals(createAccountDto.getLedgerId()))
			.findFirst()
			.orElseThrow(LedgerNotFoundException::new);

		Account account = financeMapper.mapCreateAccountDtoToAccount(createAccountDto);

		if (createAccountDto.getBalance() == null) {
			account.setBalance(BigDecimal.ZERO);
		}

		if (ledger.getAccounts().stream().anyMatch(l -> l.getName().equalsIgnoreCase(createAccountDto.getName()))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account name must be unique");
		}

		ledger.getAccounts().add(account);

		accountRepository.save(account);

		return ResponseEntity.status(HttpStatus.CREATED).body(financeMapper.mapAccountToGetAccountDto(account));
	}

	@Override
	public ResponseEntity<?> updateAccount(UpdateAccountDto updateAccountDto, Principal principal) {
		Ledger ledger = ledgerService.getLedgerEntity(updateAccountDto.getLedgerId(), principal);
		Account account = getAccountEntity(updateAccountDto.getLedgerId(), updateAccountDto.getId(), principal);

		if (updateAccountDto.getName() != null) {
			if (updateAccountDto.getName().isBlank() || updateAccountDto.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is empty");
			}

			if (ledger.getAccounts().stream().anyMatch(a -> a.getName().equalsIgnoreCase(updateAccountDto.getName()))) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already have an account by that name");
			}

			account.setName(updateAccountDto.getName());
		}

		if (updateAccountDto.getBalance() != null) {
			account.setBalance(updateAccountDto.getBalance());
		}

		if (updateAccountDto.getAccountType() != null) {
			account.setAccountType(updateAccountDto.getAccountType());
		}

		accountRepository.save(account);
		return ResponseEntity.status(HttpStatus.OK).body(financeMapper.mapAccountToGetAccountDto(account));
	}

	@Override
	public String deleteAccount(Long ledgerId, Long accountId, Principal principal) {
		accountRepository.delete(getAccountEntity(ledgerId, accountId, principal));
		return "account deleted";
	}
}
