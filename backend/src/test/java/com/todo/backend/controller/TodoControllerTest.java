package com.todo.backend.controller;

import com.todo.backend.dto.CreateTodoRequestDTO;
import com.todo.backend.dto.TodoResponseDTO;
import com.todo.backend.dto.UpdateTodoRequestDTO;
import com.todo.backend.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private TodoResponseDTO sampleDto() {
        TodoResponseDTO dto = new TodoResponseDTO();
        dto.setId(1L);
        dto.setTitle("Sample");
        dto.setDescription("Desc");
        dto.setCompleted(false);
        dto.setImportant(false);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void getAll_shouldReturnOk() throws Exception {
        when(service.getAll("all")).thenReturn(List.of(sampleDto()));

        mockMvc.perform(get("/api/todos?filter=all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void create_shouldReturn201() throws Exception {
        when(service.create(any(CreateTodoRequestDTO.class))).thenReturn(sampleDto());

        CreateTodoRequestDTO req = new CreateTodoRequestDTO();
        req.setTitle("New Todo");
        req.setDescription("D");

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample"));
    }

    @Test
    void create_invalid_shouldReturn400() throws Exception {
        // title is blank -> should fail @NotBlank in DTO
        String body = """
                {"title":"","description":"x"}
                """;

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        when(service.update(eq(1L), any(UpdateTodoRequestDTO.class))).thenReturn(sampleDto());

        UpdateTodoRequestDTO req = new UpdateTodoRequestDTO();
        req.setTitle("Updated");
        req.setDescription("Updated D");

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void toggleCompleted_shouldReturnOk() throws Exception {
        when(service.toggle(1L)).thenReturn(sampleDto());

        mockMvc.perform(patch("/api/todos/1/toggle"))
                .andExpect(status().isOk());
    }

    @Test
    void toggleImportant_shouldReturnOk() throws Exception {
        when(service.toggleImportant(1L)).thenReturn(sampleDto());

        mockMvc.perform(patch("/api/todos/1/important"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}