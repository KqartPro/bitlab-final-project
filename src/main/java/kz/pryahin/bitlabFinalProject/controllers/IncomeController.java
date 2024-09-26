package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.CreateIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.GetIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.UpdateIncomeDto;
import kz.pryahin.bitlabFinalProject.enums.TransactionSortingTypes;
import kz.pryahin.bitlabFinalProject.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
	private final IncomeService incomeService;

	@Operation(summary = "Возвращает конкретный доход пользователя",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/get-income")
	public GetIncomeDto getIncome(@RequestParam("ledgerId") Long ledgerId,
	                              @RequestParam("accountId") Long accountId,
	                              @RequestParam("incomeId") Long incomeId,
	                              Principal principal) {
		return incomeService.getIncome(ledgerId, accountId, incomeId, principal);
	}

	@Operation(summary = "Сортирует все доходы в ledger по заданному параметру, desc=true сортирует по убыванию",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/sort-incomes")
	public ResponseEntity<?> sortAllIncomes(@RequestParam("ledgerId") Long ledgerId,
	                                        @RequestParam("incomeSortingType") TransactionSortingTypes incomeSortingType,
	                                        @RequestParam("desc") boolean desc,
	                                        Principal principal) {
		return incomeService.sortAllIncomes(ledgerId, incomeSortingType, desc, principal);
	}

	@Operation(summary = "Возвращает все доходы пользователя в конкретном ledger",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/get-all-incomes")
	List<GetIncomeDto> getAllIncomes(@RequestParam("ledgerId") Long ledgerId, Principal principal) {
		return incomeService.getAllIncomes(ledgerId, principal);
	}

	@Operation(summary = "Создает новый доход",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PostMapping("/create")
	public ResponseEntity<?> createIncome(@Valid @RequestBody CreateIncomeDto createIncomeDto, Principal principal) {
		return incomeService.createIncome(createIncomeDto, principal);
	}

	@Operation(summary = """
		1) Обновляет один или несколько параметров конкретного дохода. 2)
		если incomeCategoryName передан назначит или обновит категорию. 3) если income SubCategoryId = NotNull и incomeSubCategoryName = NotNull то метод заменит имя существующей категории. 4) если incomeSubCategoryId = Null и incomeSubCategoryName = NotNull то метод создаст новую подкатегорию""",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PatchMapping("/update")
	ResponseEntity<?> updateIncome(@Valid @RequestBody UpdateIncomeDto updateIncomeDto, Principal principal) {
		return incomeService.updateIncome(updateIncomeDto, principal);
	}

	@Operation(summary = "Удаляет конкретный доход",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("/delete")
	String deleteIncome(@RequestParam("ledgerId") Long ledgerId,
	                    @RequestParam("accountId") Long accountId,
	                    @RequestParam("incomeId") Long incomeId,
	                    Principal principal) {
		return incomeService.deleteIncome(ledgerId, accountId, incomeId, principal);
	}
}
