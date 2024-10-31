package hello.todolist.controller.category;

import hello.todolist.controller.task.AddTaskForm;
import hello.todolist.domain.Category;
import hello.todolist.domain.User;
import hello.todolist.service.CategoryService;
import hello.todolist.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/new")
    public String createCategoryForm(@ModelAttribute("categoryForm") CategoryForm form) {
        return "category";
    }

    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute CategoryForm form, BindingResult result, HttpSession session) {
        String cateName = form.getCateName();
        String loginId = session.getAttribute("loginUser").toString();
        categoryService.createCategory(cateName, loginId);
        return "redirect:/";
    }

    @PostMapping("/{cateName}/update")
    public String updateCateName(@PathVariable("cateName") String cateName, @Valid @ModelAttribute CategoryForm form,
                                 BindingResult result, HttpSession session) {
        Category findCategory = categoryService.getCategory(cateName);

        if (result.hasErrors()) {
            return "/tasks";
        }

        if (getLoginUser(session) != findCategory.getUser()) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        categoryService.updateCateName(findCategory, form.getCateName());
        return "redirect:/task";
    }

    @PostMapping("/{cateName}/delete")
    public String deleteCategory(@PathVariable("cateName") String cateName, BindingResult result, HttpSession session) {
        Category findCategory = categoryService.getCategory(cateName);

        if (result.hasErrors()) {
            return "/tasks";
        }

        if (getLoginUser(session) != findCategory.getUser()) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        categoryService.deleteCategory(findCategory);
        return "redirect:/tasks";
    }

    private User getLoginUser(HttpSession session) {
        String loginId = session.getAttribute("loginUser").toString();

        return userService.findUserByLoginId(loginId);
    }
}
