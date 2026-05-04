package dev.ewerton.todolist.domain.service;

import dev.ewerton.todolist.api.dto.TaskRequestDTO;
import dev.ewerton.todolist.api.dto.TaskResponseDTO;
import dev.ewerton.todolist.domain.entity.Task;
import dev.ewerton.todolist.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(dto.status())
                .dueDate(dto.dueDate())
                .build();

        task = taskRepository.save(task);

        return mapToResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findByTitle(String title) {
        Task task = taskRepository.findByTitleContainingIgnoreCase(title).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        return mapToResponse(task);
    }

    @Transactional
    public TaskResponseDTO completeTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        task.setStatus("COMPLETED");
        return mapToResponse(taskRepository.save(task));
    }

    @Transactional
    public boolean deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }

        taskRepository.deleteById(id);
        return true;
    }

    private TaskResponseDTO mapToResponse(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt()
        );
    }
}
