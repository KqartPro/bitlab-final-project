package kz.pryahin.bitlabFinalProject.services;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IncomeCategoryService {
	ResponseEntity<String> deleteIncomeCategory(Long ledgerId, Long accountId, Long incomeId, Principal principal);
}
