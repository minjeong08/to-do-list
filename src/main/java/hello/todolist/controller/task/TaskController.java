package hello.todolist.controller.task;

import hello.todolist.domain.*;
import hello.todolist.service.TaskService;
import hello.todolist.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final AuthService authService;
    private final TaskService taskService;

    @GetMapping
    public String tasks(Model model, HttpSession session) {

        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        String loginId = getLoginId(session);
        List<Task> all = taskService.getSortedTaskList(loginId);
        model.addAttribute("all", all);

        User user = getLoginUser(session);
        List<Category> categories = user.getCategories();
        model.addAttribute("categories", categories);
        return "task/all";
    }

    @GetMapping("/new")
    public String addTaskForm(Model model, HttpSession session) {
        List<Category> categories = getUserCategories(session);

        model.addAttribute("categories", categories);
        model.addAttribute("addForm", new AddTaskForm());
        return "task/add";
    }

    @PostMapping("/new")
    public String addTask(@ModelAttribute("addForm") @Valid AddTaskForm form,
                          BindingResult result, HttpSession session, Model model,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            List<Category> categories = getUserCategories(session);
            model.addAttribute("categories", categories);
            return "/task/add";
        }

        User loginUser = getLoginUser(session);

        TaskDto dto = new TaskDto(loginUser, form.getCategoryName(), form.getTitle(),
                form.getDescription(), form.getDueDate(),
                form.getIsPriority()  ? Priority.STARRED : Priority.NONE);

        taskService.createTask(dto);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/tasks";
    }

    @GetMapping("/{taskId}")
    public String taskDetail(@PathVariable("taskId") Long id, Model model) {
        Optional<Task> task = taskService.getTask(id);

        if (task.isEmpty()) {
            return "redirect:/tasks";
        }

        model.addAttribute("updateForm", new UpdateTaskForm());
        model.addAttribute("task", task.get());
        return "task/detail";
    }

    @PostMapping("/{taskId}/update")
    public String updateTask(@ModelAttribute("updateForm") @Valid UpdateTaskForm form,
                             BindingResult result,
                             @PathVariable("taskId") Long taskId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }

            for (FieldError fieldError : result.getFieldErrors()) {
                System.out.println("Field: " + fieldError.getField());
                System.out.println("Error: " + fieldError.getDefaultMessage());
            }
            return "redirect:/tasks/{taskId}";
        }

        User loginUser = getLoginUser(session);

        TaskDto dto = new TaskDto(loginUser, form.getCategoryName(), form.getTitle(), form.getDescription(), form.getDueDate());

        taskService.updateTask(taskId, dto);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/tasks/{taskId}";
    }

    @PutMapping("/{taskId}/priority")
    public ResponseEntity<Map<String, Object>> updatePriority(@PathVariable("taskId") Long taskId,
                                                              @RequestBody Map<String, String> request,
                                                              HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Priority priority = Priority.valueOf(request.get("priority"));

        Task findTask = taskService.getTask(taskId).get();
        if (!getLoginUser(session).equals(findTask.getUser())) {
            response.put("success", false);
            response.put("message", "수정할 권한이 없습니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (!findTask.getPriority().equals(priority)) {
            taskService.updatePriority(findTask, priority);
        } else {
            response.put("success", false);
            response.put("message", "priority 수정 에러 발생");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("success", true);
        response.put("message", "priority 수정 성공!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable("taskId") Long taskId,
                                                              @RequestBody Map<String, String> request,
                                                              HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Status status = Status.valueOf(request.get("status"));

        Task findTask = taskService.getTask(taskId).get();
        if (!getLoginUser(session).equals(findTask.getUser())) {
            response.put("success", false);
            response.put("message", "수정할 권한이 없습니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (!findTask.getStatus().equals(status)) {
            taskService.updateStatus(findTask, status);
        } else {
            response.put("success", false);
            response.put("message", "status 수정 에러 발생");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("success", true);
        response.put("message", "status 수정 성공!");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}/delete")
    public ResponseEntity<Map<String, Object>> removeTask(@PathVariable("taskId") Long taskId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Task findTask = taskService.getTask(taskId).get();
        if (!getLoginUser(session).equals(findTask.getUser())) {
            response.put("success", false);
            response.put("message", "삭제할 권한이 없습니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        taskService.deleteTask(taskId);
        response.put("success", true);
        response.put("message", "삭제 성공!");
        return ResponseEntity.ok(response);
    }

    private static String getLoginId(HttpSession session) {
        return session.getAttribute("loginUser").toString();
    }

    private User getLoginUser(HttpSession session) {
        String loginId = getLoginId(session);
        return authService.findUserByLoginId(loginId);
    }

    private List<Category> getUserCategories(HttpSession session) {
        String loginId = getLoginId(session);
        User user = authService.findUserByLoginId(loginId);
        return user.getCategories();
    }
}
