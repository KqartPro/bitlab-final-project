package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.CreateExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.GetExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.UpdateExpenseDto;
import kz.pryahin.bitlabFinalProject.enums.TransactionSortingTypes;
import kz.pryahin.bitlabFinalProject.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {
	private final ExpenseService expenseService;

	@Operation(summary = "Возвращает конкретный расход пользователя",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/get-expense")
	public GetExpenseDto getExpense(@RequestParam("ledgerId") Long ledgerId,
	                                @RequestParam("accountId") Long accountId,
	                                @RequestParam("expenseId") Long expenseId,
	                                Principal principal) {
		return expenseService.getExpense(ledgerId, accountId, expenseId, principal);
	}

	@Operation(summary = "Возвращает все расходы пользователя в конкретном ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/get-all-expenses")
	public List<GetExpenseDto> getAllExpenses(@RequestParam("ledgerId") Long ledgerId, Principal principal) {
		return expenseService.getAllExpenses(ledgerId, principal);
	}

	@Operation(summary = "Сортирует все расходы в ledger по заданному параметру, desc=true сортирует по убыванию",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/sort-expenses")
	public ResponseEntity<?> sortAllExpenses(@RequestParam("ledgerId") Long ledgerId,
	                                         @RequestParam("expenseSortingType") TransactionSortingTypes expenseSortingType,
	                                         @RequestParam("desc") boolean desc,
	                                         Principal principal) {
		return expenseService.sortAllExpenses(ledgerId, expenseSortingType, desc, principal);
	}

	@Operation(summary = "Создает новый расход",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PostMapping("/create")
	public ResponseEntity<?> createExpense(@Valid @RequestBody CreateExpenseDto createExpenseDto, Principal principal) {
		return expenseService.createExpense(createExpenseDto, principal);

	}

	@Operation(summary = """
		1) Обновляет один или несколько параметров конкретного расхода. 2)
		если expenseCategoryName передан назначит или обновит категорию. 3) если expenseSubCategoryId = NotNull и expenseSubCategoryName = NotNull то метод заменит имя существующей категории. 4) если expenseSubCategoryId = Null и expenseSubCategoryName = NotNull то метод создаст новую подкатегорию""",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PatchMapping("/update")
	public ResponseEntity<?> updateExpense(@Valid @RequestBody UpdateExpenseDto updateExpenseDto, Principal principal) {
		return expenseService.updateExpense(updateExpenseDto, principal);
	}


	@Operation(summary = "Удаляет конкретный расход",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete")
	public String deleteExpense(@RequestParam("ledgerId") Long ledgerId,
	                            @RequestParam("accountId") Long accountId,
	                            @RequestParam("expenseId") Long expenseId,
	                            Principal principal) {
		return expenseService.deleteExpense(ledgerId, accountId, expenseId, principal);
	}
}
