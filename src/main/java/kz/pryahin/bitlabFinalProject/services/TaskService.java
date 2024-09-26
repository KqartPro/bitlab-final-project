package kz.pryahin.bitlabFinalProject.services;

import kz.pryahin.bitlabFinalProject.dtos.taskDtos.CreateTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.GetTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.UpdateTaskDto;
import kz.pryahin.bitlabFinalProject.enums.TaskSortingTypes;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface TaskService {
	List<GetTaskDto> getAllTasks(Principal principal);

	GetTaskDto getTask(Long taskId, Principal principal);

	ResponseEntity<?> getTaskStatistics(Principal principal);

	ResponseEntity<?> sortTasks(TaskSortingTypes taskSortingTypes, boolean desc, Principal principal);

	ResponseEntity<?> searchTaskByName(String taskName, Principal principal);

	ResponseEntity<GetTaskDto> createTask(CreateTaskDto createTaskDto, Principal principal);

	ResponseEntity<?> updateTaskDto(UpdateTaskDto updateTaskDto, Principal principal);

	String deleteTask(Long taskId, Principal principal);

}
