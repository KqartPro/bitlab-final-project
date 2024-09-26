package kz.pryahin.bitlabFinalProject.mapper;

import kz.pryahin.bitlabFinalProject.dtos.accountDtos.CreateAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.GetAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.CreateExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.expenseDtos.GetExpenseDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.CreateIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.incomeDtos.GetIncomeDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.CreateLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.GetLedgerDto;
import kz.pryahin.bitlabFinalProject.entities.Account;
import kz.pryahin.bitlabFinalProject.entities.Expense;
import kz.pryahin.bitlabFinalProject.entities.Income;
import kz.pryahin.bitlabFinalProject.entities.Ledger;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinanceMapper {
	Expense mapCreateExpenseDtoToExpense(CreateExpenseDto createExpenseDto);

	Ledger mapCreateLedgerDtoToLedger(CreateLedgerDto createLedgerDto);

	GetLedgerDto mapLedgerToGetLedgerDto(Ledger ledger);

	List<GetLedgerDto> mapLedgersListToGetLedgersListDto(List<Ledger> ledgers);

	List<GetAccountDto> mapAccountsListToGetAccountsListDto(List<Account> accounts);

	Account mapCreateAccountDtoToAccount(CreateAccountDto createAccountDto);

	GetAccountDto mapAccountToGetAccountDto(Account account);

	GetExpenseDto mapExpenseToGetExpenseDto(Expense expense);

	GetIncomeDto mapIncomeToGetIncomeDto(Income income);

	Income mapCreateIncomeDtoToIncome(CreateIncomeDto createIncomeDto);
}
