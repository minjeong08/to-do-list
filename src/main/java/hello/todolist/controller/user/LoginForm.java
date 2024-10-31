package hello.todolist.controller.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String loginId;

    @NotEmpty(message = "패스워드는 필수 입력값입니다.")
    private String password;
}