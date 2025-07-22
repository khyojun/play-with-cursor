package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TodoManager {
    private List<Todo> todos;
    private final String SAVE_FILE = "todos.json";
    private final Gson gson;
    
    public TodoManager() {
        this.todos = new ArrayList<>();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();
        loadTodos();
    }
    
    public void addTodo(String title, String description) {
        Todo todo = new Todo(title, description);
        todos.add(todo);
        saveTodos();
        System.out.println("✅ 할일이 추가되었습니다: " + title);
    }
    
    public void listTodos() {
        if (todos.isEmpty()) {
            System.out.println("📝 등록된 할일이 없습니다.");
            return;
        }
        
        System.out.println("\n📋 할일 목록:");
        System.out.println("=".repeat(50));
        for (Todo todo : todos) {
            System.out.println(todo);
            System.out.println("-".repeat(50));
        }
    }
    
    public void listPendingTodos() {
        List<Todo> pending = todos.stream()
            .filter(todo -> !todo.isCompleted())
            .collect(Collectors.toList());
            
        if (pending.isEmpty()) {
            System.out.println("🎉 모든 할일을 완료했습니다!");
            return;
        }
        
        System.out.println("\n⏰ 미완료 할일:");
        System.out.println("=".repeat(50));
        for (Todo todo : pending) {
            System.out.println(todo);
            System.out.println("-".repeat(50));
        }
    }
    
    public void completeTodo(int id) {
        Optional<Todo> todoOpt = todos.stream()
            .filter(todo -> todo.getId() == id)
            .findFirst();
            
        if (todoOpt.isPresent()) {
            Todo todo = todoOpt.get();
            if (!todo.isCompleted()) {
                todo.markAsCompleted();
                saveTodos();
                System.out.println("✅ 할일 완료: " + todo.getTitle());
            } else {
                System.out.println("⚠️ 이미 완료된 할일입니다.");
            }
        } else {
            System.out.println("❌ ID " + id + "에 해당하는 할일을 찾을 수 없습니다.");
        }
    }
    
    public void deleteTodo(int id) {
        boolean removed = todos.removeIf(todo -> todo.getId() == id);
        if (removed) {
            saveTodos();
            System.out.println("🗑️ 할일이 삭제되었습니다.");
        } else {
            System.out.println("❌ ID " + id + "에 해당하는 할일을 찾을 수 없습니다.");
        }
    }
    
    public void showStatistics() {
        long total = todos.size();
        long completed = todos.stream().filter(Todo::isCompleted).count();
        long pending = total - completed;
        
        System.out.println("\n📊 통계:");
        System.out.println("=".repeat(30));
        System.out.println("전체 할일: " + total);
        System.out.println("완료된 할일: " + completed);
        System.out.println("미완료 할일: " + pending);
        if (total > 0) {
            double percentage = (double) completed / total * 100;
            System.out.println("완료율: " + String.format("%.1f%%", percentage));
        }
    }
    
    private void saveTodos() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            gson.toJson(todos, writer);
        } catch (IOException e) {
            System.err.println("⚠️ 할일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    private void loadTodos() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Todo>>(){}.getType();
            List<Todo> loadedTodos = gson.fromJson(reader, listType);
            if (loadedTodos != null) {
                todos = loadedTodos;
                // ID 카운터 업데이트
                OptionalInt maxId = todos.stream().mapToInt(Todo::getId).max();
                if (maxId.isPresent()) {
                    // 리플렉션을 사용하여 nextId 업데이트
                    try {
                        java.lang.reflect.Field nextIdField = Todo.class.getDeclaredField("nextId");
                        nextIdField.setAccessible(true);
                        nextIdField.setInt(null, maxId.getAsInt() + 1);
                    } catch (Exception e) {
                        System.err.println("ID 카운터 업데이트 실패: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ 할일 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}