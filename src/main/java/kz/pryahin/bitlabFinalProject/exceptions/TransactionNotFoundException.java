package kz.pryahin.bitlabFinalProject.exceptions;

public class TransactionNotFoundException extends RuntimeException {
	public TransactionNotFoundException() {
		super("Transaction not found");
	}
}