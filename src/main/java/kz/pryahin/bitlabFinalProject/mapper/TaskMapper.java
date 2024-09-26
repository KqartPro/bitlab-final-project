package kz.pryahin.bitlabFinalProject.mapper;

import kz.pryahin.bitlabFinalProject.dtos.taskDtos.CreateTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.GetTaskDto;
import kz.pryahin.bitlabFinalProject.entities.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
	GetTaskDto mapTaskToGetTaskDto(Task task);

	List<GetTaskDto> mapTaskListToGetTaskListDto(List<Task> taskList);

	Task mapCreateTaskDtoToTask(CreateTaskDto createTaskDto);

}

