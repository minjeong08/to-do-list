package hello.todolist.controller.task;

import hello.todolist.domain.Category;
import hello.todolist.domain.Priority;
import hello.todolist.domain.Status;
import hello.todolist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private User user;
    private Category category;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;

    public TaskDto(User user, Category category, String title, String description,
                   LocalDateTime dueDate, Priority priority) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public TaskDto(User user, Category category, String title, String description,
                   LocalDateTime dueDate, Priority priority, Status status) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }


}