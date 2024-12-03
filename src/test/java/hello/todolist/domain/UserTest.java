package hello.todolist.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void 카테고리_추가_성공() {
        // given
        User user = new User();
        Category category = new Category();

        // when
        user.addCategory(category);

        // then
        assertThat(user.getCategories()).contains(category);
        assertThat(category.getUser()).isEqualTo(user);
    }

    @Test
    void 설정된_유저와_불일치한_카테고리_추가() {
        // given
        User user1 = new User();
        User user2 = new User();

        Category category = new Category();
        user2.addCategory(category);

        // when, then
        assertThatThrownBy(() -> user1.addCategory(category))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("카테고리 생성자와 일치하지 않습니다");
    }

    @Test
    void 중복되는_카테고리_이름_추가() {
        // given
        User user = new User();

        Category cate1 = new Category();
        cate1.setCateName("카테고리");
        Category cate2 = new Category();
        cate2.setCateName("카테고리");

        user.addCategory(cate1);

        // when, then
        assertThatThrownBy(() -> user.addCategory(cate2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 카테고리 이름입니다.");
    }

    @Test
    void 카테고리_삭제_성공() {
        // given
        User user = new User();
        Category category = new Category();
        user.addCategory(category);

        // when
        user.removeCategory(category);

        // then
        assertThat(user.getCategories()).doesNotContain(category);
        assertThat(category.getUser()).isNull();
    }

    @Test
    void 설정된_유저와_불일치한_카테고리_삭제() {
        // given
        User user1 = new User();
        User user2 = new User();

        Category category = new Category();
        user2.addCategory(category);

        // when, then
        assertThatThrownBy(() -> user1.removeCategory(category))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 카테고리입니다.");
    }

    @Test
    void 존재하지_않는_카테고리_삭제() {
        // given
        User user = new User();
        Category category = new Category();

        // when & then
        assertThatThrownBy(() -> user.removeCategory(category))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 카테고리입니다.");
    }

    @Test
    void 할일_추가_성공() {
        // given
        User user = new User();
        Task task = new Task();

        // when
        user.addTask(task);

        // then
        assertThat(user.tasks).contains(task);
        assertThat(task.getUser()).isEqualTo(user);
    }

    @Test
    void 설정된_유저가_불일치한_할일_추가() {
        // given
        User user1 = new User();
        User user2 = new User();
        Task task = new Task();

        user2.addTask(task);

        // when & then
        assertThatThrownBy(() -> user1.addTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 접근입니다.");
    }

    @Test
    void 할일_삭제_성공() {
        // given
        User user = new User();
        Task task = new Task();
        user.addTask(task);

        // when
        user.removeTask(task);

        // then
        assertThat(user.getTasks()).doesNotContain(task);
        assertThat(task.getUser()).isNull();
    }

    @Test
    void 설정된_유저와_불일치한_할일_삭제() {
        // given
        User user1 = new User();
        User user2 = new User();

        Task task = new Task();
        user2.addTask(task);

        // when & then
        assertThatThrownBy(() -> user1.removeTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 접근입니다.");
    }
}