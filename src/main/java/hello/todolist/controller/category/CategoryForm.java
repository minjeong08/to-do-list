package hello.todolist.controller.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryForm {

    @NotEmpty(message = "카테고리 이름은 필수 입력입니다.")
    private String cateName;
}
