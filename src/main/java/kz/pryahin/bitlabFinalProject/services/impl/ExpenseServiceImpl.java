package kz.pryahin.bitlabFinalProject.services.impl;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.CreateExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.GetExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.UpdateExpenseDto;
import kz.pryahin.bitlabFinalProject.entities.*;
import kz.pryahin.bitlabFinalProject.enums.TransactionSortingTypes;
import kz.pryahin.bitlabFinalProject.exceptions.AccountNotFoundException;
import kz.pryahin.bitlabFinalProject.exceptions.SubCategoryNotFoundException;
import kz.pryahin.bitlabFinalProject.exceptions.TransactionNotFoundException;
import kz.pryahin.bitlabFinalProject.mapper.FinanceMapper;
import kz.pryahin.bitlabFinalProject.repositories.ExpenseCategoryRepository;
import kz.pryahin.bitlabFinalProject.repositories.ExpenseRepository;
import kz.pryahin.bitlabFinalProject.repositories.ExpenseSubCategoryRepository;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import kz.pryahin.bitlabFinalProject.services.AccountService;
import kz.pryahin.bitlabFinalProject.services.ExpenseService;
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
public class ExpenseServiceImpl implements ExpenseService {
	private final UserService userService;
	private final FinanceMapper financeMapper;
	private final ExpenseRepository expenseRepository;
	private final ExpenseCategoryRepository expenseCategoryRepository;
	private final ExpenseSubCategoryRepository expenseSubCategoryRepository;
	private final LedgerServiceImpl ledgerService;
	private final AccountService accountService;

	@Override
	public Expense getExpenseEntity(Long ledgerId, Long accountId, Long expenseId, Principal principal) {
		return accountService.getAccountEntity(ledgerId, accountId, principal).getExpenses().stream()
			.filter(a -> a.getId().equals(expenseId))
			.findFirst()
			.orElseThrow(TransactionNotFoundException::new);
	}

	@Override
	public GetExpenseDto getExpense(Long ledgerId, Long accountId, Long expenseId, Principal principal) {
		return financeMapper.mapExpenseToGetExpenseDto(getExpenseEntity(ledgerId, accountId, expenseId, principal));
	}

	@Override
	public ResponseEntity<?> sortAllExpenses(Long ledgerId,
	                                         TransactionSortingTypes expenseSortingType,
	                                         boolean desc,
	                                         Principal principal) {


		if (expenseSortingType == TransactionSortingTypes.NAME) {
			List<GetExpenseDto> getExpensesListDto = getAllExpenses(ledgerId, principal);

			if (desc) {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getName).reversed());
			} else {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getName));
			}

			return ResponseEntity.ok(getExpensesListDto);
		}

		if (expenseSortingType == TransactionSortingTypes.CREATE_TIME) {
			List<GetExpenseDto> getExpensesListDto = getAllExpenses(ledgerId, principal);

			if (desc) {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getCreatedAt).reversed());
			} else {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getCreatedAt));
			}

			return ResponseEntity.ok(getExpensesListDto);
		}

		if (expenseSortingType == TransactionSortingTypes.UPDATE_TIME) {
			List<GetExpenseDto> getExpensesListDto = getAllExpenses(ledgerId, principal);

			if (desc) {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getUpdatedAt).reversed());
			} else {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getUpdatedAt));
			}
			return ResponseEntity.ok(getExpensesListDto);
		}

		if (expenseSortingType == TransactionSortingTypes.AMOUNT) {
			List<GetExpenseDto> getExpensesListDto = getAllExpenses(ledgerId, principal);

			if (desc) {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getAmount).reversed());
			} else {
				getExpensesListDto.sort(Comparator.comparing(GetExpenseDto::getAmount));
			}
			return ResponseEntity.ok(getExpensesListDto);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

	}

	@Override
	public List<GetExpenseDto> getAllExpenses(Long ledgerId, Principal principal) {
		List<Account> accounts = ledgerService.getLedgerEntity(ledgerId, principal).getAccounts();

		return accounts.stream()
			.flatMap(account -> account.getExpenses().stream())
			.map(financeMapper::mapExpenseToGetExpenseDto)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ResponseEntity<?> createExpense(CreateExpenseDto createExpenseDto, Principal principal) {
		User user = userService.getUserEntity(principal.getName());

		Ledger ledger = ledgerService.getLedgerEntity(createExpenseDto.getLedgerId(), principal);

		Account account = ledger.getAccounts().stream().filter(a -> a.getId().equals(createExpenseDto.getAccountId()))
			.findFirst()
			.orElseThrow(AccountNotFoundException::new);


		if (account.getBalance().compareTo(createExpenseDto.getAmount()) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your Balance less than expense");
		}

		Expense expense = financeMapper.mapCreateExpenseDtoToExpense(createExpenseDto);


		// Create Category
		if (createExpenseDto.getCategoryName() != null) {
			if (createExpenseDto.getCategoryName().isBlank() || createExpenseDto.getCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name must not be empty");
			}
			ExpenseCategory expenseCategory = new ExpenseCategory();
			expenseCategory.setName(createExpenseDto.getCategoryName());

			expense.setExpenseCategory(expenseCategory);
			expenseCategoryRepository.save(expenseCategory);
		}

		// Create Sub-Category
		if (createExpenseDto.getSubCategoryName() != null) {
			if (createExpenseDto.getSubCategoryName().isBlank() || createExpenseDto.getSubCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub-Category name must not be empty");
			}

			if (expense.getExpenseCategory() != null) {
				ExpenseSubCategory expenseSubCategory = new ExpenseSubCategory();
				expenseSubCategory.setName(createExpenseDto.getSubCategoryName());

				expense.getExpenseCategory().getExpenseSubCategories().add(expenseSubCategory);
				expenseSubCategoryRepository.save(expenseSubCategory);
			}
		}


		account.setBalance(account.getBalance().subtract(createExpenseDto.getAmount()));
		account.getExpenses().add(expense);

		expenseRepository.save(expense);

		return ResponseEntity.status(HttpStatus.CREATED).body(financeMapper.mapExpenseToGetExpenseDto(expense));
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateExpense(UpdateExpenseDto updateExpenseDto, Principal principal) {
		Account account = accountService.getAccountEntity(updateExpenseDto.getLedgerId(), updateExpenseDto.getAccountId()
			, principal);
		Expense expense = getExpenseEntity(updateExpenseDto.getLedgerId(), updateExpenseDto.getAccountId(),
			updateExpenseDto.getId(), principal);

		if (updateExpenseDto.getName() != null) {
			if (updateExpenseDto.getName().isBlank() || updateExpenseDto.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is empty");
			}

			expense.setName(updateExpenseDto.getName());
		}

		if (updateExpenseDto.getDescription() != null) {
			expense.setDescription(updateExpenseDto.getDescription());
		}

		if (updateExpenseDto.getAmount() != null) {
			if (updateExpenseDto.getAmount().compareTo(expense.getAmount().add(account.getBalance())) > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your Balance less than expense");
			}

			account.setBalance((expense.getAmount().add(account.getBalance())).subtract(updateExpenseDto.getAmount()));
			expense.setAmount(updateExpenseDto.getAmount());
		}

		// Category
		if (updateExpenseDto.getExpenseCategoryName() != null) {
			if (updateExpenseDto.getExpenseCategoryName().isBlank() || updateExpenseDto.getExpenseCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name must not be empty");
			}

			if (expense.getExpenseCategory() == null) {
				ExpenseCategory expenseCategory = new ExpenseCategory();
				expenseCategory.setName(updateExpenseDto.getExpenseCategoryName());

				expense.setExpenseCategory(expenseCategory);
				expenseCategoryRepository.save(expenseCategory);

			} else {
				expense.getExpenseCategory().setName(updateExpenseDto.getExpenseCategoryName());
			}
		}

		// Sub-category create
		if (updateExpenseDto.getExpenseSubCategoryName() != null && updateExpenseDto.getExpenseSubCategoryId() == null) {
			if (updateExpenseDto.getExpenseSubCategoryName().isBlank() || updateExpenseDto.getExpenseSubCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub-Category name must not be empty");
			}

			ExpenseSubCategory expenseSubCategory = new ExpenseSubCategory();
			expenseSubCategory.setName(updateExpenseDto.getExpenseSubCategoryName());

			expense.getExpenseCategory().getExpenseSubCategories().add(expenseSubCategory);
			expenseSubCategoryRepository.save(expenseSubCategory);
		}

		// Sub-category find
		if (updateExpenseDto.getExpenseSubCategoryName() != null && updateExpenseDto.getExpenseSubCategoryId() != null) {
			if (updateExpenseDto.getExpenseSubCategoryName().isBlank() || updateExpenseDto.getExpenseSubCategoryName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sub-Category name must not be empty");
			}

			ExpenseSubCategory expenseSubCategory =
				expense.getExpenseCategory().getExpenseSubCategories().stream()
					.filter(l -> l.getId().equals(updateExpenseDto.getExpenseSubCategoryId()))
					.findFirst().orElseThrow(SubCategoryNotFoundException::new);

			expenseSubCategory.setName(updateExpenseDto.getExpenseSubCategoryName());
			expenseSubCategoryRepository.save(expenseSubCategory);
		}

		expenseRepository.save(expense);
		return ResponseEntity.status(HttpStatus.OK).body(financeMapper.mapExpenseToGetExpenseDto(expense));
	}

	@Override
	public String deleteExpense(Long ledgerId, Long accountId, Long expenseId, Principal principal) {
		expenseRepository.delete(getExpenseEntity(ledgerId, accountId, expenseId, principal));
		return "Expense deleted";
	}
}
