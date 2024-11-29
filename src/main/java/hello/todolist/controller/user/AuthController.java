package hello.todolist.controller.user;

import hello.todolist.domain.User;
import hello.todolist.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterForm form,
                           BindingResult result) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        if (!form.getPassword().equals(form.getPassword2())) {
            result.reject("passwordsNotMatch", "비밀번호 확인이 일치하지 않습니다.");
        }

        authService.join(form.getLoginId(), form.getPassword());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm form,
                        BindingResult result,
                        HttpSession session) {

        if (result.hasErrors()) {
            return "auth/login";
        }

        User loginUser = authService.login(form.getLoginId(), form.getPassword());

        if (loginUser == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "auth/login";
        }

        session.setAttribute("loginUser", loginUser.getLoginId());

        return "redirect:/tasks";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
