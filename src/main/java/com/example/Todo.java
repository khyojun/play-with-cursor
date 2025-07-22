package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Todo {
    private static int nextId = 1;
    
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    
    public Todo(String title, String description) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    
    public void markAsCompleted() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }
    
    public void markAsIncomplete() {
        this.completed = false;
        this.completedAt = null;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String status = completed ? "[✓]" : "[ ]";
        String completedInfo = completed ? " (완료: " + completedAt.format(formatter) + ")" : "";
        
        return String.format("%s ID:%d - %s%s\n    설명: %s\n    생성: %s", 
            status, id, title, completedInfo, description, createdAt.format(formatter));
    }
}