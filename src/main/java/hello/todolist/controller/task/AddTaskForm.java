package hello.todolist.controller.task;

import hello.todolist.domain.Category;
import hello.todolist.domain.Priority;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddTaskForm {

    @NotEmpty(message = "카테고리를 설정해 주세요.")
    private Category category;

    @NotEmpty(message = "제목은 필수 입력값입니다.")
    private String title;

    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
}
