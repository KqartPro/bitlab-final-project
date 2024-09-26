package kz.pryahin.bitlabFinalProject.exceptions;

public class LedgerNotFoundException extends RuntimeException {
	public LedgerNotFoundException() {
		super("Ledger not found");
	}
}
