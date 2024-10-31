package hello.todolist.service;

import hello.todolist.controller.task.TaskDto;
import hello.todolist.domain.Status;
import hello.todolist.domain.Task;
import hello.todolist.domain.User;
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

    public void createTask(TaskDto taskDto) {

        Task task = new Task();
        task.setUser(taskDto.getUser());
        task.setCategory(taskDto.getCategory());
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

    public List<Task> getTaskList(String loginId) {
        Optional<User> user  = userRepository.findByLoginId(loginId);

        if (user.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 사용자입니다");
        }

        Long userId = user.get().getId();

        return taskRepository.findAllById(userId);
    }

    @Transactional
    public void updateTask(Long taskId, TaskDto taskDto) {
        Task findTask = taskRepository.findById(taskId).get();

        findTask.setCategory(taskDto.getCategory());
        findTask.setTitle(taskDto.getTitle());
        findTask.setDescription(taskDto.getDescription());
        findTask.setDueDate(taskDto.getDueDate());
        findTask.setPriority(taskDto.getPriority());
        findTask.setStatus(taskDto.getStatus());
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
