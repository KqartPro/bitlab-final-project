package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.entities.Income;
import kz.pryahin.bitlabFinalProject.entities.IncomeCategory;
import kz.pryahin.bitlabFinalProject.entities.IncomeSubCategory;
import kz.pryahin.bitlabFinalProject.exceptions.SubCategoryNotFoundException;
import kz.pryahin.bitlabFinalProject.repositories.IncomeCategoryRepository;
import kz.pryahin.bitlabFinalProject.services.IncomeService;
import kz.pryahin.bitlabFinalProject.services.IncomeSubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeSubCategoryServiceImpl implements IncomeSubCategoryService {
	private final IncomeService incomeService;
	private final IncomeCategoryRepository incomeCategoryRepository;

	@Override
	public ResponseEntity<String> deleteIncomeSubCategory(Long id, Long ledgerId, Long accountId, Long incomeId, Principal principal) {

		Income income = incomeService.getIncomeEntity(ledgerId, accountId, incomeId, principal);

		if (income.getIncomeCategory() == null) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category Not Found");
		}

		IncomeCategory incomeCategory = income.getIncomeCategory();
		List<IncomeSubCategory> incomeSubCategories = incomeCategory.getIncomeSubCategories();

		IncomeSubCategory incomeSubCategory =
			incomeSubCategories.stream()
				.filter(sc -> sc.getId().equals(id))
				.findFirst()
				.orElseThrow(SubCategoryNotFoundException::new);

		incomeSubCategories.remove(incomeSubCategory);

		incomeCategory.setIncomeSubCategories(incomeSubCategories);
		incomeCategoryRepository.save(incomeCategory);

		return ResponseEntity.ok("Income Sub-Category deleted");
	}
}
