package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.dtos.accountDtos.GetAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.GetExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.GetIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.CreateLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.GetLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.UpdateLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.transactionDtos.GetTransactionStatisticsDto;
import kz.pryahin.bitlabFinalProject.entities.Account;
import kz.pryahin.bitlabFinalProject.entities.Ledger;
import kz.pryahin.bitlabFinalProject.exceptions.LedgerNotFoundException;
import kz.pryahin.bitlabFinalProject.mapper.FinanceMapper;
import kz.pryahin.bitlabFinalProject.repositories.LedgerRepository;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
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
public class LedgerServiceImpl implements LedgerService {
	private final FinanceMapper financeMapper;
	private final UserService userService;
	private final LedgerRepository ledgerRepository;


	@Override
	public Ledger getLedgerEntity(Long ledgerId, Principal principal) {
		return userService.getUserEntity(principal.getName()).getLedgers().stream()
			.filter(l -> l.getId().equals(ledgerId))
			.findFirst().orElseThrow(LedgerNotFoundException::new);
	}

	@Override
	public List<GetLedgerDto> getAllLedgers(Principal principal) {
		User user = userService.getUserEntity(principal.getName());
		List<GetLedgerDto> getLedgersListDto = financeMapper.mapLedgersListToGetLedgersListDto(user.getLedgers());
		getLedgersListDto.forEach(l -> l.setUserId(user.getId()));
		return getLedgersListDto;
	}

	@Override
	public GetLedgerDto getLedger(Long ledgerId, Principal principal) {
		GetLedgerDto getLedgerDto = financeMapper.mapLedgerToGetLedgerDto(getLedgerEntity(ledgerId, principal));
		getLedgerDto.setUserId(userService.getUserEntity(principal.getName()).getId());

		return getLedgerDto;
	}

	@Override
	public ResponseEntity<?> createLedger(CreateLedgerDto createLedgerDto, Principal principal) {

		Ledger ledger = financeMapper.mapCreateLedgerDtoToLedger(createLedgerDto);

		User user = userService.getUserEntity(principal.getName());

		if (user.getLedgers().stream().anyMatch(l -> l.getName().equalsIgnoreCase(createLedgerDto.getName()))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already have a ledger by that name");
		}

		user.getLedgers().add(ledger);
		ledgerRepository.save(ledger);

		GetLedgerDto getLedgerDto = financeMapper.mapLedgerToGetLedgerDto(ledger);
		getLedgerDto.setUserId(user.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(getLedgerDto);
	}

	@Override
	public ResponseEntity<?> updateLedger(UpdateLedgerDto updateLedgerDto, Principal principal) {
		User user = userService.getUserEntity(principal.getName());
		Ledger ledger = getLedgerEntity(updateLedgerDto.getId(), principal);

		if (updateLedgerDto.getName() != null) {
			if (updateLedgerDto.getName().isBlank() || updateLedgerDto.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is empty");
			}

			if (user.getLedgers().stream().anyMatch(l -> l.getName().equalsIgnoreCase(updateLedgerDto.getName()))) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already have a ledger by that name");
			}

			ledger.setName(updateLedgerDto.getName());
		}


		if (updateLedgerDto.getCurrency() != null) {
			ledger.setCurrency(updateLedgerDto.getCurrency());
		}

		ledgerRepository.save(ledger);

		GetLedgerDto getLedgerDto = financeMapper.mapLedgerToGetLedgerDto(ledger);
		getLedgerDto.setUserId((user.getId()));

		return ResponseEntity.status(HttpStatus.OK).body(getLedgerDto);

	}

	@Override
	public String deleteLedger(Long ledgerId, Principal principal) {
		ledgerRepository.delete(getLedgerEntity(ledgerId, principal));
		return "Ledger deleted";
	}

	@Override
	public ResponseEntity<?> getAllStatistics(Long ledgerId, Principal principal) {
		Ledger ledger = getLedgerEntity(ledgerId, principal);
		List<Account> accounts = ledger.getAccounts();
		List<GetExpenseDto> getExpensesListDto = accounts.stream()
			.flatMap(account -> account.getExpenses().stream())
			.map(financeMapper::mapExpenseToGetExpenseDto)
			.toList();

		List<GetIncomeDto> getIncomesListDto = accounts.stream()
			.flatMap(account -> account.getIncomes().stream())
			.map(financeMapper::mapIncomeToGetIncomeDto)
			.toList();

		List<GetAccountDto> getAccountsListDto = financeMapper.mapAccountsListToGetAccountsListDto(accounts);


		BigDecimal totalExpensesAmount = new BigDecimal(0);
		BigDecimal totalIncomesAmount = new BigDecimal(0);
		BigDecimal totalAccountsBalance = new BigDecimal(0);

		for (GetExpenseDto e : getExpensesListDto) {
			totalExpensesAmount = totalExpensesAmount.add(e.getAmount());
		}

		for (GetIncomeDto i : getIncomesListDto) {
			totalIncomesAmount = totalIncomesAmount.add(i.getAmount());
		}

		for (GetAccountDto a : getAccountsListDto) {
			totalAccountsBalance = totalAccountsBalance.add(a.getBalance());
		}

		GetTransactionStatisticsDto getTransactionStatisticsDto = new GetTransactionStatisticsDto();

		getTransactionStatisticsDto.setTotalExpensesAmount(totalExpensesAmount);
		getTransactionStatisticsDto.setTotalIncomesAmount(totalIncomesAmount);
		getTransactionStatisticsDto.setTotalAccountsBalance(totalAccountsBalance);
		getTransactionStatisticsDto.setDifference(totalIncomesAmount.subtract(totalExpensesAmount));

		return ResponseEntity.ok(getTransactionStatisticsDto);
	}
}
