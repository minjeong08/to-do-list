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

    public void setUser(User user) {
        this.user = user;
        if (user != null && !user.getCategories().contains(this)) {
            user.getCategories().add(this);
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        if (task.getCategory() != this) {
            task.setCategory(this);
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setCategory(null);
    }
}
