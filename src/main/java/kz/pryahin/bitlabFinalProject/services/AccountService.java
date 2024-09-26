package kz.pryahin.bitlabFinalProject.services;

import kz.pryahin.bitlabFinalProject.dtos.accountDtos.CreateAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.GetAccountDto;
import kz.pryahin.bitlabFinalProject.dtos.accountDtos.UpdateAccountDto;
import kz.pryahin.bitlabFinalProject.entities.Account;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface AccountService {
	Account getAccountEntity(Long ledgerId, Long accountId, Principal principal);

	ResponseEntity<?> createAccount(CreateAccountDto createAccountDto, Principal principal);

	GetAccountDto getAccount(Long accountId, Long ledgerId, Principal principal);

	List<GetAccountDto> getAllAccounts(Long ledgerId, Principal principal);

	ResponseEntity<?> updateAccount(UpdateAccountDto updateAccountDto, Principal principal);

	String deleteAccount(Long ledgerId, Long accountId, Principal principal);
}
