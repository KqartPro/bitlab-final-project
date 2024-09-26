package kz.pryahin.bitlabFinalProject.services.impl;

import kz.pryahin.bitlabFinalProject.dtos.taskDtos.CreateTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.GetTaskDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.GetTaskStatisticsDto;
import kz.pryahin.bitlabFinalProject.dtos.taskDtos.UpdateTaskDto;
import kz.pryahin.bitlabFinalProject.entities.Task;
import kz.pryahin.bitlabFinalProject.enums.TaskSortingTypes;
import kz.pryahin.bitlabFinalProject.exceptions.TaskNotFoundException;
import kz.pryahin.bitlabFinalProject.mapper.TaskMapper;
import kz.pryahin.bitlabFinalProject.repositories.TaskRepository;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import kz.pryahin.bitlabFinalProject.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	private final UserService userService;

	private Task getTaskEntity(Long taskId, Principal principal) {
		return userService.getUserEntity(principal.getName()).getTasks().stream()
			.filter(t -> t.getId().equals(taskId))
			.findFirst()
			.orElseThrow(TaskNotFoundException::new);
	}

	@Override
	public ResponseEntity<?> searchTaskByName(String taskName, Principal principal) {
		List<GetTaskDto> getTasksListDto = taskMapper.mapTaskListToGetTaskListDto(taskRepository.findAllByNameContainsIgnoreCaseAndUserEmail(taskName,
			principal.getName()));
		return ResponseEntity.ok(getTasksListDto);
	}

	@Override
	public ResponseEntity<?> getTaskStatistics(Principal principal) {
		List<GetTaskDto> tasks = getAllTasks(principal);

		if (tasks.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have no tasks");
		}

		long allCompleted = getAllTasks(principal).stream().filter(t -> t.getIsCompleted().equals(true)).count();
		long allUncompleted = getAllTasks(principal).stream().filter(t -> t.getIsCompleted().equals(false)).count();

		GetTaskStatisticsDto getTaskStatisticsDto = new GetTaskStatisticsDto();
		getTaskStatisticsDto.setAllCompleted(allCompleted);
		getTaskStatisticsDto.setAllUncompleted(allUncompleted);

		return ResponseEntity.ok().body(getTaskStatisticsDto);
	}

	@Override
	public ResponseEntity<?> sortTasks(TaskSortingTypes taskSortingType, boolean desc, Principal principal) {

		if (taskSortingType == TaskSortingTypes.NAME) {
			List<GetTaskDto> getTasksListDto = getAllTasks(principal);

			if (desc) {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getName).reversed());
			} else {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getName));
			}

			return ResponseEntity.ok(getTasksListDto);
		}

		if (taskSortingType == TaskSortingTypes.CREATE_TIME) {
			List<GetTaskDto> getTasksListDto = getAllTasks(principal);

			if (desc) {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getCreatedAt).reversed());
			} else {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getCreatedAt));
			}

			return ResponseEntity.ok(getTasksListDto);
		}

		if (taskSortingType == TaskSortingTypes.UPDATE_TIME) {
			List<GetTaskDto> getTasksListDto = getAllTasks(principal);

			if (desc) {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getUpdatedAt).reversed());
			} else {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getUpdatedAt));
			}
			return ResponseEntity.ok(getTasksListDto);
		}

		if (taskSortingType == TaskSortingTypes.PRIORITY) {
			List<GetTaskDto> getTasksListDto = getAllTasks(principal);

			if (desc) {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getPriority, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
			} else {
				getTasksListDto.sort(Comparator.comparing(GetTaskDto::getPriority, Comparator.nullsLast(Comparator.naturalOrder())));
			}
			return ResponseEntity.ok(getTasksListDto);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@Override
	public List<GetTaskDto> getAllTasks(Principal principal) {
		User user = userService.getUserEntity(principal.getName());
		List<GetTaskDto> getTasksListDto = taskMapper.mapTaskListToGetTaskListDto(user.getTasks());
		getTasksListDto.forEach(t -> t.setUserId(user.getId()));
		return getTasksListDto;
	}

	@Override
	public GetTaskDto getTask(Long taskId, Principal principal) {
		GetTaskDto getTaskDto = taskMapper.mapTaskToGetTaskDto(getTaskEntity(taskId, principal));
		getTaskDto.setUserId(userService.getUserEntity(principal.getName()).getId());
		return getTaskDto;

	}

	@Override
	public ResponseEntity<GetTaskDto> createTask(CreateTaskDto createTaskDto, Principal principal) {
		User user = userService.getUserEntity(principal.getName());

		Task task = taskMapper.mapCreateTaskDtoToTask(createTaskDto);
		task.setIsCompleted(false);
		user.getTasks().add(task);
		taskRepository.save(task);

		GetTaskDto getTaskDto = taskMapper.mapTaskToGetTaskDto(task);
		getTaskDto.setUserId(user.getId());


		return ResponseEntity.status(HttpStatus.CREATED).body(getTaskDto);
	}

	@Override
	public ResponseEntity<?> updateTaskDto(UpdateTaskDto updateTaskDto, Principal principal) {
		Task task = getTaskEntity(updateTaskDto.getId(), principal);

		if (updateTaskDto.getName() != null) {
			if (updateTaskDto.getName().isBlank() || updateTaskDto.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is empty");
			}
			task.setName(updateTaskDto.getName());
		}

		if (updateTaskDto.getIsCompleted() != null) {
			task.setIsCompleted(updateTaskDto.getIsCompleted());
		}
		if (updateTaskDto.getPriority() != null) {
			task.setPriority(updateTaskDto.getPriority());
		}

		taskRepository.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(taskMapper.mapTaskToGetTaskDto(task));
	}

	@Override
	public String deleteTask(Long taskId, Principal principal) {
		taskRepository.delete(getTaskEntity(taskId, principal));
		return "Task deleted";
	}
}
