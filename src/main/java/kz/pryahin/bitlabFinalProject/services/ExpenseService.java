package kz.pryahin.bitlabFinalProject.services;

import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.CreateExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.GetExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.UpdateExpenseDto;
import kz.pryahin.bitlabFinalProject.entities.Expense;
import kz.pryahin.bitlabFinalProject.enums.TransactionSortingTypes;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface ExpenseService {

	Expense getExpenseEntity(Long ledgerId, Long accountId, Long expenseId, Principal principal);

	GetExpenseDto getExpense(Long ledgerId, Long accountId, Long expenseId, Principal principal);

	ResponseEntity<?> sortAllExpenses(Long ledgerId,
	                                  TransactionSortingTypes expenseSortingType,
	                                  boolean desc,
	                                  Principal principal);

	List<GetExpenseDto> getAllExpenses(Long ledgerId, Principal principal);

	ResponseEntity<?> createExpense(CreateExpenseDto createExpenseDto, Principal principal);

	ResponseEntity<?> updateExpense(UpdateExpenseDto updateExpenseDto, Principal principal);

	String deleteExpense(Long ledgerId, Long accountId, Long expenseId, Principal principal);
}
