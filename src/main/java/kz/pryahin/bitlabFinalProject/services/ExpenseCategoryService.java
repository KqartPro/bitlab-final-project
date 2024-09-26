package kz.pryahin.bitlabFinalProject.services;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface ExpenseCategoryService {
	ResponseEntity<String> deleteExpenseCategory(Long ledgerId, Long accountId, Long expenseId, Principal principal);
}
