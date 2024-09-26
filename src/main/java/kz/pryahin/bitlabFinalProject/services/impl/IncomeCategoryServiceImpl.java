package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.entities.Income;
import kz.pryahin.bitlabFinalProject.entities.IncomeCategory;
import kz.pryahin.bitlabFinalProject.repositories.IncomeCategoryRepository;
import kz.pryahin.bitlabFinalProject.repositories.IncomeRepository;
import kz.pryahin.bitlabFinalProject.services.IncomeCategoryService;
import kz.pryahin.bitlabFinalProject.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class IncomeCategoryServiceImpl implements IncomeCategoryService {
	private final IncomeService incomeService;
	private final IncomeCategoryRepository incomeCategoryRepository;
	private final IncomeRepository incomeRepository;

	@Override
	public ResponseEntity<String> deleteIncomeCategory(Long ledgerId, Long accountId, Long incomeId,
	                                                   Principal principal) {
		Income income = incomeService.getIncomeEntity(ledgerId, accountId, incomeId, principal);


		IncomeCategory incomeCategory = income.getIncomeCategory();

		if (incomeCategory != null) {
			income.setIncomeCategory(null);
			incomeRepository.save(income);

			incomeCategoryRepository.delete(incomeCategory);
			return ResponseEntity.ok("Income Category deleted");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Income Category not found");
		}
	}
}
