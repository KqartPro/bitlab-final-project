package kz.pryahin.bitlabFinalProject.services;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface ExpenseSubCategoryService {
	ResponseEntity<String> deleteExpenseSubCategory(Long id, Long ledgerId, Long accountId, Long expenseId,
	                                                Principal principal);
}
