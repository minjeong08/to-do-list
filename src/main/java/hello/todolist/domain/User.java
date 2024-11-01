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
        categories.add(category);
        if (category.getUser() != this) {
            category.setUser(this);
        }
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.setUser(null);
    }

    public void addTask(Task task) {
        tasks.add(task);
        if (task.getUser() != this) {
            task.setUser(this);
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setUser(null);
    }
}
