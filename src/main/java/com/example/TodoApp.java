package com.example;

import java.util.Scanner;

public class TodoApp {
    private static final TodoManager todoManager = new TodoManager();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🎯 Todo List Manager에 오신 것을 환영합니다!");
        System.out.println("도움말을 보려면 'help'를 입력하세요.\n");
        
        while (true) {
            System.out.print("명령어를 입력하세요 > ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            
            try {
                switch (command) {
                    case "help", "h":
                        showHelp();
                        break;
                    case "add", "a":
                        handleAddTodo(parts);
                        break;
                    case "list", "l":
                        todoManager.listTodos();
                        break;
                    case "pending", "p":
                        todoManager.listPendingTodos();
                        break;
                    case "complete", "c":
                        handleCompleteTodo(parts);
                        break;
                    case "delete", "d":
                        handleDeleteTodo(parts);
                        break;
                    case "stats", "s":
                        todoManager.showStatistics();
                        break;
                    case "exit", "quit", "q":
                        System.out.println("👋 Todo List Manager를 종료합니다. 안녕히 가세요!");
                        return;
                    case "clear":
                        clearScreen();
                        break;
                    default:
                        System.out.println("❓ 알 수 없는 명령어입니다. 'help'를 입력하여 도움말을 확인하세요.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ 명령어 실행 중 오류가 발생했습니다: " + e.getMessage());
            }
            
            System.out.println();
        }
    }
    
    private static void showHelp() {
        System.out.println("\n📖 사용 가능한 명령어:");
        System.out.println("=".repeat(50));
        System.out.println("add (a)       - 새 할일 추가");
        System.out.println("list (l)      - 모든 할일 보기");
        System.out.println("pending (p)   - 미완료 할일 보기");
        System.out.println("complete (c)  - 할일 완료 처리 (ID 필요)");
        System.out.println("delete (d)    - 할일 삭제 (ID 필요)");
        System.out.println("stats (s)     - 통계 보기");
        System.out.println("clear         - 화면 지우기");
        System.out.println("help (h)      - 도움말 보기");
        System.out.println("exit (q)      - 프로그램 종료");
        System.out.println("=".repeat(50));
        System.out.println("\n💡 사용 예시:");
        System.out.println("add           → 대화형으로 할일 추가");
        System.out.println("complete 1    → ID 1번 할일 완료");
        System.out.println("delete 2      → ID 2번 할일 삭제");
    }
    
    private static void handleAddTodo(String[] parts) {
        System.out.println("\n➕ 새 할일 추가:");
        System.out.print("제목: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("❌ 제목은 비워둘 수 없습니다.");
            return;
        }
        
        System.out.print("설명 (선택사항): ");
        String description = scanner.nextLine().trim();
        
        if (description.isEmpty()) {
            description = "설명이 없습니다.";
        }
        
        todoManager.addTodo(title, description);
    }
    
    private static void handleCompleteTodo(String[] parts) {
        if (parts.length < 2) {
            System.out.println("❌ 완료할 할일의 ID를 입력해주세요. 예: complete 1");
            return;
        }
        
        try {
            int id = Integer.parseInt(parts[1]);
            todoManager.completeTodo(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효한 ID를 입력해주세요.");
        }
    }
    
    private static void handleDeleteTodo(String[] parts) {
        if (parts.length < 2) {
            System.out.println("❌ 삭제할 할일의 ID를 입력해주세요. 예: delete 1");
            return;
        }
        
        try {
            int id = Integer.parseInt(parts[1]);
            System.out.print("정말 삭제하시겠습니까? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if (confirm.equals("y") || confirm.equals("yes")) {
                todoManager.deleteTodo(id);
            } else {
                System.out.println("❌ 삭제가 취소되었습니다.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효한 ID를 입력해주세요.");
        }
    }
    
    private static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // 화면 지우기가 실패해도 프로그램은 계속 실행
            System.out.print("\033[2J\033[H"); // ANSI 이스케이프 시퀀스로 화면 지우기
        }
    }
}