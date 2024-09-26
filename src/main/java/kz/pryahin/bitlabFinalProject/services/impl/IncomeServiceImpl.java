package kz.pryahin.bitlabFinalProject.services.impl;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.CreateIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.GetIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.UpdateIncomeDto;
import kz.pryahin.bitlabFinalProject.entities.*;
import kz.pryahin.bitlabFinalProject.enums.TransactionSortingTypes;
import kz.pryahin.bitlabFinalProject.exceptions.AccountNotFoundException;
import kz.pryahin.bitlabFinalProject.exceptions.SubCategoryNotFoundException;
import kz.pryahin.bitlabFinalProject.exceptions.TransactionNotFoundException;
import kz.pryahin.bitlabFinalProject.mapper.FinanceMapper;
import kz.pryahin.bitlabFinalProject.repositories.IncomeCategoryRepository;
import kz.pryahin.bitlabFinalProject.repositories.IncomeRepository;
import kz.pryahin.bitlabFinalProject.repositories.IncomeSubCategoryRepository;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import kz.pryahin.bitlabFinalProject.services.AccountService;
import kz.pryahin.bitlabFinalProject.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
	private final UserService userService;
	private final LedgerServiceImpl ledgerService;
	private final FinanceMapper financeMapper;
	private final IncomeCategoryRepository incomeCategoryRepository;
	private final IncomeSubCategoryRepository incomeSubCategoryRepository;
	private final IncomeRepository incomeRepository;
	private final AccountService accountService;

	@Override
	public Income getIncomeEntity(Long ledgerId, Long accountId, Long incomeId, Principal principal) {
		return accountService.getAccountEntity(ledgerId, accountId, principal).getIncomes().stream()
			.filter(a -> a.getId().equals(incomeId))
			.findFirst()
			.orElseThrow(TransactionNotFoundException::new);
	}

	@Override
	public GetIncomeDto getIncome(Long ledgerId, Long accountId, Long incomeId, Principal principal) {
		return financeMapper.mapIncomeToGetIncomeDto(getIncomeEntity(ledgerId, accountId, incomeId, principal));
	}

	@Override
	public ResponseEntity<?> sortAllIncomes(Long ledgerId, TransactionSortingTypes incomeSortingType, boolean desc, Principal principal) {
		if (incomeSortingType == TransactionSortingTypes.NAME) {
			List<GetIncomeDto> getIncomesListDto = getAllIncomes(ledgerId, principal);

			if (desc) {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getName).reversed());
			} else {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getName));
			}

			return ResponseEntity.ok(getIncomesListDto);
		}

		if (incomeSortingType == TransactionSortingTypes.CREATE_TIME) {
			List<GetIncomeDto> getIncomesListDto = getAllIncomes(ledgerId, principal);

			if (desc) {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getCreatedAt).reversed());
			} else {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getCreatedAt));
			}

			return ResponseEntity.ok(getIncomesListDto);
		}

		if (incomeSortingType == TransactionSortingTypes.UPDATE_TIME) {
			List<GetIncomeDto> getIncomesListDto = getAllIncomes(ledgerId, principal);

			if (desc) {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getUpdatedAt).reversed());
			} else {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getUpdatedAt));
			}
			return ResponseEntity.ok(getIncomesListDto);
		}

		if (incomeSortingType == TransactionSortingTypes.AMOUNT) {
			List<GetIncomeDto> getIncomesListDto = getAllIncomes(ledgerId, principal);

			if (desc) {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getAmount).reversed());
			} else {
				getIncomesListDto.sort(Comparator.comparing(GetIncomeDto::getAmount));
			}
			return ResponseEntity.ok(getIncomesListDto);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@Override
	public List<GetIncomeDto> getAllIncomes(Long ledgerId, Principal principal) {
		List<Account> accounts = ledgerService.getLedgerEntity(ledgerId, principal).getAccounts();

		return accounts.stream()
			.flatMap(account -> account.getIncomes().stream())
			.map(financeMapper::mapIncomeToGetIncomeDto)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ResponseEntity<?> createIncome(CreateIncomeDto createIncomeDto, Principal principal) {
		User user = userService.getUserEntity(principal.getName());

		Ledger ledger = ledgerService.getLedgerEntity(createIncomeDto.getLedgerId(), principal);

		Account account = ledger.getAccounts().stream().filter(a -> a.getId().equals(createIncomeDto.getAccountId()))
			.findFirst()
			.orElseThrow(AccountNotFoundException::new);

		Income income = financeMapper.mapCreateIncomeDtoToIncome(createIncomeDto);

		// Create Category
		if (createIncomeDto.getCategoryName() != null) {
			if (createIncomeDto.getCategoryName().isBlank() || createIncomeDto.getCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name must not be empty");
			}

			IncomeCategory incomeCategory = new IncomeCategory();
			incomeCategory.setName(createIncomeDto.getCategoryName());

			income.setIncomeCategory(incomeCategory);
			incomeCategoryRepository.save(incomeCategory);
		}

		// Create Sub-Category
		if (createIncomeDto.getSubCategoryName() != null) {
			if (createIncomeDto.getSubCategoryName().isBlank() || createIncomeDto.getSubCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub-Category name must not be empty");
			}

			if (income.getIncomeCategory() != null) {
				IncomeSubCategory incomeSubCategory = new IncomeSubCategory();
				incomeSubCategory.setName(createIncomeDto.getSubCategoryName());

				income.getIncomeCategory().getIncomeSubCategories().add(incomeSubCategory);
				incomeSubCategoryRepository.save(incomeSubCategory);
			}
		}

		account.setBalance(account.getBalance().add(createIncomeDto.getAmount()));
		account.getIncomes().add(income);

		incomeRepository.save(income);

		return ResponseEntity.status(HttpStatus.CREATED).body(financeMapper.mapIncomeToGetIncomeDto(income));
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateIncome(UpdateIncomeDto updateIncomeDto, Principal principal) {
		Account account = accountService.getAccountEntity(updateIncomeDto.getLedgerId(), updateIncomeDto.getAccountId()
			, principal);
		Income income = getIncomeEntity(updateIncomeDto.getLedgerId(), updateIncomeDto.getAccountId(),
			updateIncomeDto.getId(), principal);

		if (updateIncomeDto.getName() != null) {
			if (updateIncomeDto.getName().isBlank() || updateIncomeDto.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is empty");
			}

			income.setName(updateIncomeDto.getName());
		}

		if (updateIncomeDto.getDescription() != null) {
			income.setDescription(updateIncomeDto.getDescription());
		}

		if (updateIncomeDto.getAmount() != null) {
			account.setBalance(account.getBalance().subtract(income.getAmount()).add(updateIncomeDto.getAmount()));
			income.setAmount(updateIncomeDto.getAmount());
		}

		// Category
		if (updateIncomeDto.getIncomeCategoryName() != null) {
			if (updateIncomeDto.getIncomeCategoryName().isBlank() || updateIncomeDto.getIncomeCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name must not be empty");
			}

			if (income.getIncomeCategory() == null) {
				IncomeCategory incomeCategory = new IncomeCategory();
				incomeCategory.setName(updateIncomeDto.getIncomeCategoryName());

				income.setIncomeCategory(incomeCategory);
				incomeCategoryRepository.save(incomeCategory);
			} else {
				income.getIncomeCategory().setName(updateIncomeDto.getIncomeCategoryName());
			}
		}

		// Create Sub-category
		if (updateIncomeDto.getIncomeSubCategoryName() != null && updateIncomeDto.getIncomeSubCategoryId() == null) {
			if (updateIncomeDto.getIncomeSubCategoryName().isBlank() || updateIncomeDto.getIncomeSubCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub-Category name must not be empty");
			}

			IncomeSubCategory incomeSubCategory = new IncomeSubCategory();
			incomeSubCategory.setName(updateIncomeDto.getIncomeSubCategoryName());

			income.getIncomeCategory().getIncomeSubCategories().add(incomeSubCategory);
			incomeSubCategoryRepository.save(incomeSubCategory);
		}

		// Find Sub-category
		if (updateIncomeDto.getIncomeSubCategoryName() != null && updateIncomeDto.getIncomeSubCategoryId() != null) {
			if (updateIncomeDto.getIncomeSubCategoryName().isBlank() || updateIncomeDto.getIncomeSubCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub-Category name must not be empty");
			}

			IncomeSubCategory incomeSubCategory =
				income.getIncomeCategory().getIncomeSubCategories().stream()
					.filter(l -> l.getId().equals(updateIncomeDto.getIncomeSubCategoryId()))
					.findFirst().orElseThrow(SubCategoryNotFoundException::new);

			incomeSubCategory.setName(updateIncomeDto.getIncomeSubCategoryName());

			incomeSubCategoryRepository.save(incomeSubCategory);

		}

		incomeRepository.save(income);
		return ResponseEntity.status(HttpStatus.OK).body(financeMapper.mapIncomeToGetIncomeDto(income));
	}

	@Override
	public String deleteIncome(Long ledgerId, Long accountId, Long incomeId, Principal principal) {
		incomeRepository.delete(getIncomeEntity(ledgerId, accountId, incomeId, principal));
		return "Income deleted";
	}
}