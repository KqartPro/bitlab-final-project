package kz.pryahin.bitlabFinalProject.dtos.taskDtos;

import jakarta.validation.constraints.NotNull;
import kz.pryahin.bitlabFinalProject.enums.Priority;
import lombok.Data;

@Data
public class UpdateTaskDto {
	@NotNull
	private Long id;

	private String name;

	private Boolean isCompleted;

	private Priority priority;

}
