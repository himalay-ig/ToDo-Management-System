package com.todo.backend.service.impl;

import com.todo.backend.dto.CreateTodoRequestDTO;
import com.todo.backend.dto.TodoResponseDTO;
import com.todo.backend.dto.UpdateTodoRequestDTO;
import com.todo.backend.entity.Todo;
import com.todo.backend.exception.ResourceNotFoundException;
import com.todo.backend.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository repo;

    private TodoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TodoServiceImpl(repo);
    }

    @Test
    void create_shouldSaveAndReturnDto() {
        CreateTodoRequestDTO dto = new CreateTodoRequestDTO();
        dto.setTitle("Test Todo");
        dto.setDescription("Desc");

        Todo saved = new Todo();
        saved.setId(1L);
        saved.setTitle("Test Todo");
        saved.setDescription("Desc");
        saved.setCompleted(false);
        saved.setImportant(false);

        when(repo.save(any(Todo.class))).thenReturn(saved);

        TodoResponseDTO response = service.create(dto);

        assertEquals("Test Todo", response.getTitle());
        assertEquals("Desc", response.getDescription());
        assertEquals(false, response.getCompleted());
        assertEquals(false, response.getImportant());
        verify(repo, times(1)).save(any(Todo.class));
    }

    @Test
    void getAll_filterCompleted_shouldUseFindByCompletedTrue() {
        Todo t = new Todo();
        t.setId(1L);
        t.setTitle("C1");
        t.setCompleted(true);
        t.setImportant(false);

        when(repo.findByCompleted(true)).thenReturn(List.of(t));

        List<TodoResponseDTO> result = service.getAll("completed");

        assertEquals(1, result.size());
        assertEquals(true, result.get(0).getCompleted());
        verify(repo, times(1)).findByCompleted(true);
        verify(repo, never()).findAll();
    }

    @Test
    void getAll_filterPending_shouldUseFindByCompletedFalse() {
        Todo t = new Todo();
        t.setId(2L);
        t.setTitle("P1");
        t.setCompleted(false);
        t.setImportant(false);

        when(repo.findByCompleted(false)).thenReturn(List.of(t));

        List<TodoResponseDTO> result = service.getAll("pending");

        assertEquals(1, result.size());
        assertEquals(false, result.get(0).getCompleted());
        verify(repo, times(1)).findByCompleted(false);
        verify(repo, never()).findAll();
    }

    @Test
    void getAll_filterAll_shouldUseFindAll() {
        when(repo.findAll()).thenReturn(List.of());

        List<TodoResponseDTO> result = service.getAll("all");

        assertNotNull(result);
        verify(repo, times(1)).findAll();
        verify(repo, never()).findByCompleted(anyBoolean());
    }

    @Test
    void getAll_unknownFilter_shouldFallbackToFindAll() {
        when(repo.findAll()).thenReturn(List.of());

        List<TodoResponseDTO> result = service.getAll("something");

        assertNotNull(result);
        verify(repo, times(1)).findAll();
    }

    @Test
    void toggle_shouldFlipCompleted() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("T");
        todo.setCompleted(false);
        todo.setImportant(false);

        when(repo.findById(1L)).thenReturn(Optional.of(todo));
        when(repo.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

        TodoResponseDTO response = service.toggle(1L);

        assertEquals(true, response.getCompleted());
        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(any(Todo.class));
    }

    @Test
    void toggleImportant_shouldFlipImportant() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("T");
        todo.setCompleted(false);
        todo.setImportant(false);

        when(repo.findById(1L)).thenReturn(Optional.of(todo));
        when(repo.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

        TodoResponseDTO response = service.toggleImportant(1L);

        assertEquals(true, response.getImportant());
        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(any(Todo.class));
    }

    @Test
    void update_shouldUpdateTitleAndDescription() {
        Todo existing = new Todo();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setDescription("OldD");
        existing.setCompleted(false);
        existing.setImportant(false);

        UpdateTodoRequestDTO dto = new UpdateTodoRequestDTO();
        dto.setTitle("New");
        dto.setDescription("NewD");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

        TodoResponseDTO response = service.update(1L, dto);

        assertEquals("New", response.getTitle());
        assertEquals("NewD", response.getDescription());

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        verify(repo).save(captor.capture());
        assertEquals("New", captor.getValue().getTitle());
    }

    @Test
    void delete_shouldCallRepoDeleteWhenFound() {
        Todo existing = new Todo();
        existing.setId(1L);
        existing.setTitle("Del");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(repo).delete(existing);

        service.delete(1L);

        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).delete(existing);
    }

    @Test
    void toggle_shouldThrowWhenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.toggle(99L));

        verify(repo, times(1)).findById(99L);
        verify(repo, never()).save(any());
    }

    @Test
    void toggleImportant_shouldThrowWhenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.toggleImportant(99L));

        verify(repo, times(1)).findById(99L);
        verify(repo, never()).save(any());
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        verify(repo, times(1)).findById(99L);
        verify(repo, never()).delete(any());
    }
}