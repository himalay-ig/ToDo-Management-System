package com.todo.backend.controller;

import com.todo.backend.dto.*;
import com.todo.backend.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponseDTO create(@Valid @RequestBody CreateTodoRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<TodoResponseDTO> getAll(@RequestParam(defaultValue = "all") String filter) {
        return service.getAll(filter);
    }

    @PutMapping("/{id}")
    public TodoResponseDTO update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateTodoRequestDTO dto) {
        return service.update(id, dto);
    }

    // ✅ Toggle completed
    @PatchMapping("/{id}/toggle")
    public TodoResponseDTO toggle(@PathVariable Long id) {
        return service.toggle(id);
    }

    // ✅ NEW: Toggle important (Option B)
    @PatchMapping("/{id}/important")
    public TodoResponseDTO toggleImportant(@PathVariable Long id) {
        return service.toggleImportant(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}