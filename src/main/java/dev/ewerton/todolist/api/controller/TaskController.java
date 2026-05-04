package dev.ewerton.todolist.api.controller;

import dev.ewerton.todolist.api.dto.TaskRequestDTO;
import dev.ewerton.todolist.api.dto.TaskResponseDTO;
import dev.ewerton.todolist.domain.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(
            @RequestBody
            @Valid
            TaskRequestDTO taskDTO
    ){
        TaskResponseDTO createdTask = taskService.create(taskDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll(){
        return ResponseEntity.ok(taskService.findAll());
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> complete(@PathVariable UUID id){
        return ResponseEntity.ok(taskService.completeTask(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
