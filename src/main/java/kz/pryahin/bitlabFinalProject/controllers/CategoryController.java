package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.pryahin.bitlabFinalProject.services.ExpenseCategoryService;
import kz.pryahin.bitlabFinalProject.services.ExpenseSubCategoryService;
import kz.pryahin.bitlabFinalProject.services.IncomeCategoryService;
import kz.pryahin.bitlabFinalProject.services.IncomeSubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
	private final ExpenseCategoryService expenseCategoryService;
	private final IncomeCategoryService incomeCategoryService;
	private final ExpenseSubCategoryService expenseSubCategoryService;
	private final IncomeSubCategoryService incomeSubCategoryService;

	/* Создание/Обновление категории и саб-категории происходит при создании/обновление транзакции в ExpenseController
	или IncomeController */

	@Operation(summary = "Удаляет категорию конкретного расхода",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete-expense-category")
	public ResponseEntity<String> deleteExpenseCategory(@RequestParam("ledgerId") Long ledgerId,
	                                                    @RequestParam("accountId") Long accountId,
	                                                    @RequestParam("expenseId") Long expenseId,
	                                                    Principal principal) {
		return expenseCategoryService.deleteExpenseCategory(ledgerId, accountId, expenseId, principal);
	}

	@Operation(summary = "Удаляет категорию конкретного дохода",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete-income-category")
	public ResponseEntity<String> deleteIncomeCategory(@RequestParam("ledgerId") Long ledgerId,
	                                                   @RequestParam("accountId") Long accountId,
	                                                   @RequestParam("incomeId") Long incomeId,
	                                                   Principal principal) {
		return incomeCategoryService.deleteIncomeCategory(ledgerId, accountId, incomeId, principal);
	}

	@Operation(summary = "Удаляет под-категорию конкретного расхода",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete-expense-sub-category")
	public ResponseEntity<String> deleteExpenseSubCategory(@RequestParam("id") Long id,
	                                                       @RequestParam("ledgerId") Long ledgerId,
	                                                       @RequestParam("accountId") Long accountId,
	                                                       @RequestParam("expenseId") Long expenseId,
	                                                       Principal principal) {
		return expenseSubCategoryService.deleteExpenseSubCategory(id, ledgerId, accountId, expenseId, principal);
	}

	@Operation(summary = "Удаляет под-категорию конкретного дахода",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete-income-sub-category")
	public ResponseEntity<String> deleteIncomeSubCategory(@RequestParam("id") Long id,
	                                                      @RequestParam("ledgerId") Long ledgerId,
	                                                      @RequestParam("accountId") Long accountId,
	                                                      @RequestParam("incomeId") Long incomeId,
	                                                      Principal principal) {
		return incomeSubCategoryService.deleteIncomeSubCategory(id, ledgerId, accountId, incomeId, principal);
	}
}
