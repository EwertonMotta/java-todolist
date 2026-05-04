package dev.ewerton.todolist.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record TaskRequestDTO(

    @NotBlank(message = "O título não pode estar em branco")
    String title,

    String description,

    @NotBlank(message = "O status não pode estar em branco")
    String status,

    @FutureOrPresent(message = "A data de vencimento não pode ser no passado")
    LocalDateTime dueDate
) {}
