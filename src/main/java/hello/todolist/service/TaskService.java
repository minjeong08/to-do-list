package hello.todolist.service;

import hello.todolist.controller.task.TaskDto;
import hello.todolist.domain.*;
import hello.todolist.repository.CategoryRepository;
import hello.todolist.repository.TaskRepository;
import hello.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void createTask(TaskDto taskDto) {

        Task task = new Task();
        task.setUser(taskDto.getUser());
        task.getUser().addTask(task);

        String cateName = taskDto.getCategoryName();
        Category category = categoryRepository.findByCateName(cateName);
        task.setCategory(category);
        category.addTask(task);

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
    }

    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getSortedTaskList(String loginId) {
        Optional<User> user  = userRepository.findByLoginId(loginId);

        if (user.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 사용자입니다");
        }

        Long userId = user.get().getId();

        return taskRepository.findAllSortedByPriority(userId);
    }

    @Transactional
    public void updateTask(Long taskId, TaskDto taskDto) {
        Task findTask = taskRepository.findById(taskId).get();

        String cateName = taskDto.getCategoryName();
        Category category = categoryRepository.findByCateName(cateName);

        if (!findTask.getCategory().getCateName().equals(cateName)) {
            findTask.getCategory().removeTask(findTask);
        }
        findTask.setCategory(category);

        findTask.setTitle(taskDto.getTitle());
        findTask.setDescription(taskDto.getDescription());
        findTask.setDueDate(taskDto.getDueDate());
        findTask.setPriority(taskDto.getPriority());
        findTask.setStatus(taskDto.getStatus());
    }

    @Transactional
    public void updatePriority(Task task, Priority priority) {
        task.setPriority(priority);
    }

    @Transactional
    public void updateStatus(Task task, Status status) {
        task.setStatus(status);
    }

    public void deleteTask(Long taskId) {
        Optional<Task> findTask = taskRepository.findById(taskId);

        if (findTask.isPresent()) {
            Task task = findTask.get();
            task.getUser().removeTask(task);
            task.getCategory().removeTask(task);
            task.setUser(null);
            task.setCategory(null);
        }

        taskRepository.deleteById(taskId);
    }
}
