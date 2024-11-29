package hello.todolist.service;

import hello.todolist.domain.Category;
import hello.todolist.domain.User;
import hello.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;

    public User join(String loginId, String password) {

        List<User> findUsers = repository.findByLoginId(loginId).stream().toList();

        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

        User user = new User();
        String hashedPassword = PasswordUtil.hashPassword(password);
        user.setLoginId(loginId);
        user.setPassword(hashedPassword);

        return repository.save(user);
    }

    public User login(String loginId, String password) {
        return repository.findByLoginId(loginId)
                .filter(u -> PasswordUtil.verifyPassword(password, u.getPassword()))
                .orElse(null);
    }

    public User findUserByLoginId(String loginId) {
        return repository.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
    }

    public List<Category> getCategories(String loginId) {
        return repository.findByLoginId(loginId).get().getCategories();
    }
}