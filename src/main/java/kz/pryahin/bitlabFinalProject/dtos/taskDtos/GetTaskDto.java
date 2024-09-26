package kz.pryahin.bitlabFinalProject.dtos.taskDtos;

import kz.pryahin.bitlabFinalProject.enums.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetTaskDto {
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean isCompleted;
	private Long userId;
	private Priority priority;
}
