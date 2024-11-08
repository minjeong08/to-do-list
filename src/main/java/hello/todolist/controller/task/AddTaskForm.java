package hello.todolist.controller.task;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AddTaskForm {

    @NotEmpty(message = "카테고리를 설정해 주세요.")
    private String categoryName;

    @NotEmpty(message = "제목은 필수 입력값입니다.")
    private String title;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private Boolean isPriority;
}
