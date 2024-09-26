package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.entities.Expense;
import kz.pryahin.bitlabFinalProject.entities.ExpenseCategory;
import kz.pryahin.bitlabFinalProject.entities.ExpenseSubCategory;
import kz.pryahin.bitlabFinalProject.exceptions.SubCategoryNotFoundException;
import kz.pryahin.bitlabFinalProject.repositories.ExpenseCategoryRepository;
import kz.pryahin.bitlabFinalProject.services.ExpenseService;
import kz.pryahin.bitlabFinalProject.services.ExpenseSubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseSubCategoryServiceImpl implements ExpenseSubCategoryService {
	private final ExpenseService expenseService;
	private final ExpenseCategoryRepository expenseCategoryRepository;

	@Override
	public ResponseEntity<String> deleteExpenseSubCategory(Long id, Long ledgerId, Long accountId, Long expenseId,
	                                                       Principal principal) {
		Expense expense = expenseService.getExpenseEntity(ledgerId, accountId, expenseId, principal);

		if (expense.getExpenseCategory() == null) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category Not Found");
		}

		ExpenseCategory expenseCategory = expense.getExpenseCategory();
		List<ExpenseSubCategory> expenseSubCategories = expenseCategory.getExpenseSubCategories();

		ExpenseSubCategory expenseSubCategory =
			expenseSubCategories.stream()
				.filter(sc -> sc.getId().equals(id))
				.findFirst()
				.orElseThrow(SubCategoryNotFoundException::new);

		expenseSubCategories.remove(expenseSubCategory);

		expenseCategory.setExpenseSubCategories(expenseSubCategories);
		expenseCategoryRepository.save(expenseCategory);

		return ResponseEntity.ok("Expense Sub-Category deleted");
	}
}
