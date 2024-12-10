package hello.todolist.service;

import hello.todolist.controller.task.TaskDto;
import hello.todolist.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired private TaskService taskService;
    @Autowired private AuthService authService;
    @Autowired private CategoryService categoryService;

    private User user;
    private String categoryName;

    @BeforeEach
    void setup() {
        String loginId = "testId";
        String password = "testPassword";
        user = authService.join(loginId, password);

        categoryName = "testCategory";
        categoryService.createCategory(categoryName, loginId);
    }

    @Test
    void 할일_추가() {
        // given
        String title = "title";
        String description = "desc";
        LocalDate dueDate = LocalDate.now();
        Priority priority = Priority.STARRED;
        TaskDto taskDto = new TaskDto(user, categoryName, title, description, dueDate, priority);

        // when
        taskService.createTask(taskDto);

        // then
        Task task = user.getTasks().get(0);
        assertThat(task.getUser()).isEqualTo(user);
        assertThat(task.getCategory().getCateName()).isEqualTo(categoryName);
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getPriority()).isEqualTo(priority);
    }

    @Test
    void 잘못된_taskDto로_할일_추가() {
        // given
        String title = "title";
        String cateName = "Unknown";

        String description = "desc";
        LocalDate dueDate = LocalDate.now();Priority priority = Priority.STARRED;
        TaskDto invalidTaskDto = new TaskDto(user, cateName, title, description, dueDate, priority);

        // when & then
        assertThatThrownBy(() -> taskService.createTask(invalidTaskDto))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void id로_task_가져오기() {
        // given
        String title = "testTitle";
        String description = "testDesc";
        LocalDate dueDate = LocalDate.now();
        Priority priority = Priority.STARRED;
        TaskDto taskDto = new TaskDto(user, categoryName, title, description, dueDate, priority);
        taskService.createTask(taskDto);
        Task saveTask = user.getTasks().get(0);

        // when
        Long taskId = saveTask.getId();
        Task findTask = taskService.getTask(taskId).orElseThrow();

        // then
        assertThat(findTask.getUser()).isEqualTo(user);
        assertThat(findTask.getCategory().getCateName()).isEqualTo(categoryName);
        assertThat(findTask.getTitle()).isEqualTo(title);
        assertThat(findTask.getDescription()).isEqualTo(description);
        assertThat(findTask.getDueDate()).isEqualTo(dueDate);
        assertThat(findTask.getPriority()).isEqualTo(priority);
    }

    @Test
    void 존재하지_않는_task_조회() {
        // when
        Optional<Task> result = taskService.getTask(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 정렬된_리스트_가져오기() {
        // given
        for (int i = 1; i < 6; i++) {
            String title = String.valueOf(i);
            String description = String.valueOf(i * -1);
            LocalDate dueDate = LocalDate.now();
            Priority priority = (i % 2 == 1 ? Priority.STARRED : Priority.NONE);
            TaskDto taskDto = new TaskDto(user, categoryName, title, description, dueDate, priority);
            taskService.createTask(taskDto);
        }

        // when
        List<Task> sortedList = taskService.getSortedTaskList(user.getLoginId());

        // then
        assertThat(sortedList).extracting("title").containsExactly("1", "3", "5", "2", "4");
        assertThat(sortedList).extracting("description").containsExactly("-1", "-3", "-5", "-2", "-4");
    }

    @Test
    void 존재하지_않는_유저의_정렬_리스트_가져오기() {
        // given
        String loginId = "unknown";

        // when & then
        assertThatThrownBy(() -> taskService.getSortedTaskList(loginId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다");

    }

    @Test
    void task_수정하기() {
        // given
        Task task = createTestTask();
        Long taskId = task.getId();

        // when
        String title2 = "update title";
        String description2 = "update desc";
        LocalDate dueDate2 = LocalDate.now();
        TaskDto updateTaskDto = new TaskDto(user, categoryName, title2, description2, dueDate2);
        taskService.updateTask(taskId, updateTaskDto);

        // then
        assertThat(task.getTitle()).isEqualTo(title2);
        assertThat(task.getDescription()).isEqualTo(description2);
        assertThat(task.getDueDate()).isEqualTo(dueDate2);
    }

    @Test
    void 존재하지_않는_task_수정() {
        // given
        String title = "update title";
        String description = "update desc";
        LocalDate dueDate = LocalDate.now();
        Priority priority = Priority.NONE;
        TaskDto taskDto = new TaskDto(user, categoryName, title, description, dueDate, priority);

        // when & then
        assertThatThrownBy(() -> taskService.updateTask(999L, taskDto))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 수정할_dto_전달하지_않고_수정_시도() {
        // given
        Task task = createTestTask();
        Long taskId = task.getId();

        // when & then
        assertThatThrownBy(() -> taskService.updateTask(taskId, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 우선순위_변경하기() {
        // given
        Task task = createTestTask();

        // when
        Priority newPriority = Priority.NONE;
        taskService.updatePriority(task, newPriority);

        // then
        assertThat(task.getPriority()).isEqualTo(newPriority);
    }

    @Test
    void task가_null이면_우선순위_변경시_예외_발생() {
        // given
        Priority priority = Priority.NONE;

        // when & then
        assertThatThrownBy(() -> taskService.updatePriority(null, priority))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void task_상태_변경하기() {
        // given
        Task task = createTestTask();

        // when
        Status newStatus = Status.COMPLETED;
        taskService.updateStatus(task, newStatus);

        // then
        assertThat(task.getStatus()).isEqualTo(newStatus);
    }

    @Test
    void task가_null이면_상태_변경시_예외_발생() {
        // given
        Status status = Status.COMPLETED;

        // when & then
        assertThatThrownBy(() -> taskService.updateStatus(null, status))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 삭제하기() {
        // given
        Task task = createTestTask();

        // when
        taskService.deleteTask(task.getId());

        // then
        Category category = user.getCategories().get(0);
        assertThat(user.getTasks()).doesNotContain(task);
        assertThat(category.getTasks()).doesNotContain(task);
    }

    private Task createTestTask() {
        String title = "title";
        String description = "desc";
        LocalDate dueDate = LocalDate.now();
        Priority priority = Priority.STARRED;
        TaskDto taskDto = new TaskDto(user, categoryName, title, description, dueDate, priority);
        taskService.createTask(taskDto);
        return user.getTasks().get(0);
    }
}