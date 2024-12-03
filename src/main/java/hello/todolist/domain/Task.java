package hello.todolist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity
public class Task {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String title;

    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setUser(User user) {
        if (user != null && user.getTasks().contains(this)) {
            throw new IllegalStateException("이미 등록된 todo 입니다");
        }

        this.user = user;
    }

    public void setCategory(Category category) {
        if (category != null && category.getTasks().contains(this)) {
            throw new IllegalStateException("이미 등록된 todo 입니다");
        }

        this.category = category;
    }
}
