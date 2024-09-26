package kz.pryahin.bitlabFinalProject.dtos.taskDtos;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import kz.pryahin.bitlabFinalProject.enums.Priority;
import lombok.Data;

@Data
public class CreateTaskDto {

	@NotBlank
	private String name;

	@Enumerated
	private Priority priority;

}
