package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.entities.Expense;
import kz.pryahin.bitlabFinalProject.entities.ExpenseCategory;
import kz.pryahin.bitlabFinalProject.repositories.ExpenseCategoryRepository;
import kz.pryahin.bitlabFinalProject.repositories.ExpenseRepository;
import kz.pryahin.bitlabFinalProject.services.ExpenseCategoryService;
import kz.pryahin.bitlabFinalProject.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
	private final ExpenseService expenseService;
	private final ExpenseCategoryRepository expenseCategoryRepository;
	private final ExpenseRepository expenseRepository;

	@Override
	public ResponseEntity<String> deleteExpenseCategory(Long ledgerId, Long accountId, Long expenseId, Principal principal) {
		Expense expense = expenseService.getExpenseEntity(ledgerId, accountId, expenseId, principal);


		ExpenseCategory expenseCategory = expense.getExpenseCategory();

		if (expenseCategory != null) {
			expense.setExpenseCategory(null);
			expenseRepository.save(expense);

			expenseCategoryRepository.delete(expenseCategory);
			return ResponseEntity.ok("Expense Category deleted");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expense Category not found");
		}

	}
}

