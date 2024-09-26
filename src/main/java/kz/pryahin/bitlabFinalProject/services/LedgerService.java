package kz.pryahin.bitlabFinalProject.services;

import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.CreateLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.GetLedgerDto;
import kz.pryahin.bitlabFinalProject.dtos.ledgerDtos.UpdateLedgerDto;
import kz.pryahin.bitlabFinalProject.entities.Ledger;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface LedgerService {
	Ledger getLedgerEntity(Long id, Principal principal);

	ResponseEntity<?> createLedger(CreateLedgerDto createLedgerDto, Principal principal);

	List<GetLedgerDto> getAllLedgers(Principal principal);

	GetLedgerDto getLedger(Long ledgerId, Principal principal);

	ResponseEntity<?> updateLedger(UpdateLedgerDto updateLedgerDto, Principal principal);

	String deleteLedger(Long ledgerId, Principal principal);

	ResponseEntity<?> getAllStatistics(Long ledgerId, Principal principal);
}
