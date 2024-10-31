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


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Task> tasks = new ArrayList<>();
}
