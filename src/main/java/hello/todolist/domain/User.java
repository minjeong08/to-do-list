package hello.todolist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;

    private String loginId;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> tasks = new ArrayList<>();

    public void addCategory(Category category) {

        if (category.getUser() != null && category.getUser() != this) {
            throw new IllegalStateException("카테고리 생성자와 일치하지 않습니다");
        }

        if (categories.stream().anyMatch(c -> c.getCateName().equals(category.getCateName()))) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }

        categories.add(category);
        if (category.getUser() != this) {
            category.setUser(this);
        }
    }

    public void removeCategory(Category category) {

        if (!categories.contains(category) || category == null || category.getUser() != this) {
            throw new IllegalStateException("존재하지 않는 카테고리입니다.");
        }

        categories.remove(category);
        category.setUser(null);
    }

    public void addTask(Task task) {
        if (task.getUser() != null && task.getUser() != this) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(Task task) {
        if (!tasks.contains(task) || task == null || task.getUser() != this) {
            throw new IllegalStateException("잘못된 접근입니다.");
        }

        tasks.remove(task);
        task.setUser(null);
    }
}
