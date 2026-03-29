package com.todo.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateTodoRequestDTO {

    @NotBlank
    private String title;

    private String description;

    // ✅ getters & setters (IMPORTANT)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}