package kz.pryahin.bitlabFinalProject.dtos.taskDtos;

import lombok.Data;

@Data
public class GetTaskStatisticsDto {
	private long allCompleted;
	private long allUncompleted;
}
