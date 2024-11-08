package hello.todolist.service;

import hello.todolist.domain.Category;
import hello.todolist.domain.User;
import hello.todolist.repository.CategoryRepository;
import hello.todolist.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void createCategory(String cateName, String loginId) {
        Optional<User> loginUser = userRepository.findByLoginId(loginId);

        if (loginUser.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 사용자입니다");
        }

        Category cate = new Category();
        cate.setCateName(cateName);
        cate.setUser(loginUser.get());
        cate.getUser().addCategory(cate);
        categoryRepository.save(cate);
    }


    public Category findCategoryById(Long cateId) {
        return categoryRepository.findById(cateId).get();
    }

    @Transactional
    public void updateCateName(Category category, String newCateName) {
        category.setCateName(newCateName);
    }

    public void deleteCategory(Category cate) {
        cate.getUser().removeCategory(cate);
        categoryRepository.delete(cate);
    }
}
