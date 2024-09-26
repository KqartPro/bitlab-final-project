package kz.pryahin.bitlabFinalProject.exceptions;

public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException() {
		super("Category not found");
	}
}
