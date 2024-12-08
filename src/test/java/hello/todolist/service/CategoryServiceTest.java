package hello.todolist.service;

import hello.todolist.domain.Category;
import hello.todolist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired private CategoryService categoryService;
    @Autowired private AuthService authService;

    private User user;

    @BeforeEach
    void 유저_생성() {
        String loginId = "testId";
        String password = "testPassword";
        user = authService.join(loginId, password);
    }

    @Test
    void 카테고리_생성() {
        // given
        String categoryName = "testName";

        // when
        categoryService.createCategory(categoryName, user.getLoginId());

        // then
        assertThat(user.getCategories()).extracting("cateName").contains(categoryName);
    }

    @Test
    void 존재하지_않는_유저가_카테고리_생성() {
        // given
        String categoryName = "testName";
        String loginId = "unknown";

        // when & then
        assertThatThrownBy(() -> categoryService.createCategory(categoryName, loginId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다");
    }

    @Test
    void id로_카테고리_찾기() {
        // given
        String categoryName = "testName";
        categoryService.createCategory(categoryName, user.getLoginId());

        Category saveCategory = user.getCategories().get(0);
        Long categoryId = saveCategory.getId();

        // when
        Category findCategory = categoryService.findCategoryById(categoryId);

        // then
        assertThat(saveCategory.getUser()).isEqualTo(findCategory.getUser());
        assertThat(saveCategory.getCateName()).isEqualTo(findCategory.getCateName());
    }

    @Test
    void 존재하지_않는_카테고리_찾기() {
        // when & then
        assertThatThrownBy(() -> categoryService.findCategoryById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 카테고리_이름_수정() {
        // given
        String currentName = "current";
        categoryService.createCategory(currentName, user.getLoginId());
        Category category = user.getCategories().get(0);

        String newName = "new";

        // when
        categoryService.updateCateName(category, newName);

        // then
        assertThat(category.getCateName()).isEqualTo(newName);
    }

    @Test
    void null_카테고리_이름_수정() {
        // when & then
        assertThatThrownBy(() -> categoryService.updateCateName(null, "newName"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 정상_카테고리_삭제() {
        // given
        String categoryName = "testName";
        categoryService.createCategory(categoryName, user.getLoginId());

        Category category = user.getCategories().get(0);

        // when
        categoryService.deleteCategory(category);

        // then
        assertThat(user.getCategories()).doesNotContain(category);
    }

    @Test
    void null카테고리_삭제하면_예외발생() {
        // when & then
        assertThatThrownBy(() -> categoryService.deleteCategory(null))
                .isInstanceOf(NullPointerException.class);
    }
}