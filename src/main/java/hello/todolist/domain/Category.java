package hello.todolist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Category {

    @Id @GeneratedValue
    private Long id;

    private String cateName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        if (task.getCategory() != null && task.getCategory() != this) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }
        task.setCategory(this);
        tasks.add(task);
    }

    public void removeTask(Task task) {
        if (!tasks.contains(task) || task == null) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        task.setCategory(null);
        tasks.remove(task);
    }
}
