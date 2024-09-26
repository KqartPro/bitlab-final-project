package kz.pryahin.bitlabFinalProject.services;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IncomeSubCategoryService {
	ResponseEntity<String> deleteIncomeSubCategory(Long id, Long ledgerId, Long accountId, Long incomeId, Principal principal);
}
