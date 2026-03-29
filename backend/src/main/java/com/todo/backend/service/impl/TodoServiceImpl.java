package com.todo.backend.service.impl;

import com.todo.backend.dto.*;
import com.todo.backend.entity.Todo;
import com.todo.backend.exception.ResourceNotFoundException;
import com.todo.backend.repository.TodoRepository;
import com.todo.backend.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repo;

    public TodoServiceImpl(TodoRepository repo) {
        this.repo = repo;
    }

    @Override
    public TodoResponseDTO create(CreateTodoRequestDTO dto) {
        Todo todo = new Todo();
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        // completed & important default to false (entity handles it)
        return mapToDTO(repo.save(todo));
    }

    @Override
    public List<TodoResponseDTO> getAll(String filter) {
        List<Todo> todos;

        if ("completed".equalsIgnoreCase(filter)) {
            todos = repo.findByCompleted(true);
        } else if ("pending".equalsIgnoreCase(filter)) {
            todos = repo.findByCompleted(false);
        } else {
            todos = repo.findAll();
        }

        return todos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TodoResponseDTO update(Long id, UpdateTodoRequestDTO dto) {
        Todo todo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        return mapToDTO(repo.save(todo));
    }

    @Override
    public TodoResponseDTO toggle(Long id) {
        Todo todo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        todo.setCompleted(!todo.getCompleted());
        return mapToDTO(repo.save(todo));
    }

    // ✅ NEW: toggle important
    @Override
    public TodoResponseDTO toggleImportant(Long id) {
        Todo todo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        todo.setImportant(!todo.getImportant());
        return mapToDTO(repo.save(todo));
    }

    @Override
    public void delete(Long id) {
        Todo todo = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        repo.delete(todo);
    }

    // ✅ Mapper updated to include important
    private TodoResponseDTO mapToDTO(Todo t) {
        TodoResponseDTO dto = new TodoResponseDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setCompleted(t.getCompleted());
        dto.setImportant(t.getImportant());   // ✅ IMPORTANT ADDED
        dto.setCreatedAt(t.getCreatedAt());
        return dto;
    }
}