package hello.todolist.service;

import hello.todolist.domain.Category;
import hello.todolist.domain.Task;
import hello.todolist.domain.User;
import hello.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User join(String loginId, String password) {

        List<User> findUsers = repository.findByLoginId(loginId).stream().toList();

        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

        User user = new User();
        user.setLoginId(loginId);
        user.setPassword(password);

        return repository.save(user);
    }

    public User login(String loginId, String password) {
        return repository.findByLoginId(loginId)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    public User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
    }

    public User findUserByLoginId(String loginId) {
        return repository.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
    }

    public List<Task> getTasks(String loginId) {
        return repository.findByLoginId(loginId).get().getTasks();
    }

    public List<Category> getCategories(String loginId) {
        return repository.findByLoginId(loginId).get().getCategories();
    }
}