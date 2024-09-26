package kz.pryahin.bitlabFinalProject.exceptions;

public class SubCategoryNotFoundException extends RuntimeException {
	public SubCategoryNotFoundException() {
		super("Sub-category not found");
	}
}
