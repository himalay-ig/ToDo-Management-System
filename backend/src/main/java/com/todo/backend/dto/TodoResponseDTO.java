package com.todo.backend.dto;

import java.time.LocalDateTime;

public class TodoResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private Boolean important;      // ✅ NEW FIELD
    private LocalDateTime createdAt;

    // ✅ getters + setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    // ✅ IMPORTANT getters/setters
    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}