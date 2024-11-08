package hello.todolist.controller.task;

import hello.todolist.domain.Priority;
import hello.todolist.domain.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskForm {
    @NotEmpty(message = "카테고리를 설정해 주세요.")
    private String categoryName;

    @NotEmpty(message = "제목은 필수 입력값입니다.")
    private String title;

    private String description;
    private LocalDate dueDate;
//    private Priority priority;
//    private Status status;
}