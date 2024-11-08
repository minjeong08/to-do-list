package hello.todolist.controller.task;

import hello.todolist.domain.Priority;
import hello.todolist.domain.Status;
import hello.todolist.domain.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private User user;
    private String categoryName;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Status status;

    public TaskDto(User user, String categoryName, String title, String description,
                   LocalDate dueDate, Priority priority) {
        this.user = user;
        this.categoryName = categoryName;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public TaskDto(User user, String categoryName, String title, String description,
                   LocalDate dueDate, Priority priority, Status status) {
        this.user = user;
        this.categoryName = categoryName;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public TaskDto(User user, String categoryName, String title, String description, LocalDate dueDate) {
        this.user = user;
        this.categoryName = categoryName;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}