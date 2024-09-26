package kz.pryahin.bitlabFinalProject.services;

import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.CreateIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.GetIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.UpdateIncomeDto;
import kz.pryahin.bitlabFinalProject.entities.Income;
import kz.pryahin.bitlabFinalProject.enums.TransactionSortingTypes;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface IncomeService {
	Income getIncomeEntity(Long ledgerId, Long accountId, Long incomeId, Principal principal);

	GetIncomeDto getIncome(Long ledgerId, Long accountId, Long incomeId, Principal principal);

	ResponseEntity<?> sortAllIncomes(Long ledgerId, TransactionSortingTypes incomeSortingType, boolean desc, Principal principal);

	List<GetIncomeDto> getAllIncomes(Long ledgerId, Principal principal);

	ResponseEntity<?> createIncome(CreateIncomeDto createIncomeDto, Principal principal);

	ResponseEntity<?> updateIncome(UpdateIncomeDto updateIncomeDto, Principal principal);

	String deleteIncome(Long ledgerId, Long accountId, Long incomeId, Principal principal);


}
