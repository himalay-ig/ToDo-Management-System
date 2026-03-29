package com.todo.backend.service;

import com.todo.backend.dto.*;
import java.util.List;

public interface TodoService {

    TodoResponseDTO create(CreateTodoRequestDTO dto);

    List<TodoResponseDTO> getAll(String filter);

    TodoResponseDTO update(Long id, UpdateTodoRequestDTO dto);

    TodoResponseDTO toggle(Long id);

    // ✅ NEW: toggle important
    TodoResponseDTO toggleImportant(Long id);

    void delete(Long id);
}