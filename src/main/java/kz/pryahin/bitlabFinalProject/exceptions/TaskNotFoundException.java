package kz.pryahin.bitlabFinalProject.exceptions;

public class TaskNotFoundException extends RuntimeException {
	public TaskNotFoundException() {
		super("Task not found");
	}
}
