package hello.todolist.controller.task;

import hello.todolist.domain.Category;
import hello.todolist.domain.Task;
import hello.todolist.domain.User;
import hello.todolist.service.TaskService;
import hello.todolist.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping
    public String tasks(Model model, HttpSession session) {

        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        String loginId = session.getAttribute("loginUser").toString();

        List<Task> tasks = taskService.getTaskList(loginId);
        model.addAttribute("tasks", tasks);
        return "task/all";
    }

    @GetMapping("/new")
    public String addTaskForm(Model model, HttpSession session) {
        String loginId = session.getAttribute("loginUser").toString();
        List<Category> categories = userService.getCategories(loginId);

        model.addAttribute("categories", categories);
        model.addAttribute("addForm", new AddTaskForm());
        return "task/add";
    }

    @PostMapping("/new")
    public String addTask(@Valid AddTaskForm form,
                          BindingResult result,
                          HttpSession session) {

        if (result.hasErrors()) {
            return "redirect:/tasks/new";
        }

        User loginUser = getLoginUser(session);

        TaskDto dto = new TaskDto(loginUser, form.getCategory(), form.getTitle(),
                form.getDescription(), form.getDueDate(), form.getPriority());

        taskService.createTask(dto);
        return "redirect:/tasks";
    }

    @PostMapping("/new2")
    @ResponseBody
    public String addTask2(@Valid AddTaskForm form,
                          BindingResult result,
                          HttpSession session) {

        if (result.hasErrors()) {
            return "실패";
        }

        User loginUser = getLoginUser(session);

        TaskDto dto = new TaskDto(loginUser, form.getCategory(), form.getTitle(),
                                  form.getDescription(), form.getDueDate(), form.getPriority());

        return dto.toString();
    }

    @GetMapping("/{taskId}")
    public String oneTask(@PathVariable("taskId") Long id, Model model) {
        Optional<Task> task = taskService.getTask(id);

        if (task.isEmpty()) {
            return "redirect:/tasks";
        }

        model.addAttribute("task", task);

        return "task/detail";
    }

    @GetMapping("/{taskId}/update")
    public String updateTaskForm(Model model) {
        model.addAttribute("updateForm", new UpdateTaskForm());
        return "task/update";
    }

    @PostMapping("/{taskId}/update")
    public String updateTask(@Valid UpdateTaskForm form,
                             BindingResult result,
                             @PathVariable("taskId") Long taskId,
                             HttpSession session) {

        if (result.hasErrors()) {
            return "task/update";
        }

        User loginUser = getLoginUser(session);

        TaskDto dto = new TaskDto(loginUser, form.getCategory(), form.getTitle(),
                form.getDescription(), form.getDueDate(), form.getPriority(), form.getStatus());

        taskService.updateTask(taskId, dto);
        return "redirect:/tasks/{taskId}";
    }

    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }


    private User getLoginUser(HttpSession session) {
        String loginId = session.getAttribute("loginUser").toString();
        return userService.findUserByLoginId(loginId);
    }
}
