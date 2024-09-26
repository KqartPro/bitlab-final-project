package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.CreateTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.GetTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.UpdateTaskDto;
import kz.pryahin.bitlabFinalProject.enums.TaskSortingTypes;
import kz.pryahin.bitlabFinalProject.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
	private final TaskService taskService;

	@Operation(summary = "Возвращает все задачи пользователя", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/get-all-tasks")
	public List<GetTaskDto> getAllTasks(Principal principal) {
		return taskService.getAllTasks(principal);
	}

	@Operation(summary = "Возвращает конкретную задачу пользователя", security = @SecurityRequirement(name =
		"bearerAuth"))
	@GetMapping("/get-task")
	public GetTaskDto getTask(@RequestParam("taskId") Long taskId, Principal principal) {
		return taskService.getTask(taskId, principal);
	}

	@Operation(summary = "Сортирует задачу по введенному параметру, при получение desc = true, сортирует по убыванию",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("/sort-tasks")
	public ResponseEntity<?> sortTasks(@RequestParam("taskSortingType") TaskSortingTypes taskSortingType,
	                                   @RequestParam("desc") boolean desc,
	                                   Principal principal) {
		return taskService.sortTasks(taskSortingType, desc, principal);
	}

	@Operation(summary = "Находит задачи которые содержат в имени введенный параметр, игнорирую case",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("search-tasks")
	public ResponseEntity<?> searchTasks(@RequestParam("taskName") String taskName, Principal principal) {
		return taskService.searchTaskByName(taskName, principal);
	}

	@Operation(summary = "Возвращает статистику по задачам пользователя, при условии что они есть",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@GetMapping("statistics")
	public ResponseEntity<?> getTaskStatistics(Principal principal) {
		return taskService.getTaskStatistics(principal);
	}

	@Operation(summary = "Создает новую задачу",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PostMapping("/create")
	public ResponseEntity<GetTaskDto> createTask(@Valid @RequestBody CreateTaskDto createTaskDto, Principal principal) {
		return taskService.createTask(createTaskDto, principal);
	}

	@Operation(summary = "Обновляет один или несколько введенных параметров задачи",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@PatchMapping("/update")
	public ResponseEntity<?> updateTask(@Valid @RequestBody UpdateTaskDto updateTaskDto, Principal principal) {
		return taskService.updateTaskDto(updateTaskDto, principal);
	}

	@Operation(summary = "Удаляет задачу",
		security = @SecurityRequirement(name =
			"bearerAuth"))
	@DeleteMapping("delete")
	public String deleteTask(@RequestParam("taskId") Long taskId, Principal principal) {
		return taskService.deleteTask(taskId, principal);
	}
}
