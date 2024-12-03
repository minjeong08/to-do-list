package hello.todolist.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void 할일_추가_성공() {
        // given
        Category category = new Category();
        Task task = new Task();

        // when
        category.addTask(task);

        // then
        assertThat(category.getTasks()).contains(task);
        assertThat(task.getCategory()).isEqualTo(category);
    }

    @Test
    void 다른_카테고리로_설정된_할일_추가() {
        // given
        Category category1 = new Category();
        Category category2 = new Category();
        Task task = new Task();
        category2.addTask(task);

        // when & then
        assertThatThrownBy(() -> category1.addTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 접근입니다.");
    }

    @Test
    void 할일_삭제_성공() {
        // given
        Category category = new Category();
        Task task = new Task();
        category.addTask(task);

        // when
        category.removeTask(task);

        // then
        assertThat(category.getTasks()).doesNotContain(task);
        assertThat(task.getCategory()).isNull();
    }

    @Test
    void 카테고리에_포함되지_않은_할일_삭제() {
        // given
        Category category1 = new Category();
        Category category2 = new Category();
        Task task = new Task();
        category2.addTask(task);

        // when & then
        assertThatThrownBy(() -> category1.removeTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 접근입니다.");
    }

    @Test
    void null로_전달된_할일_삭제() {
        // given
        Category category = new Category();
        Task task = null;

        // when & then
        assertThatThrownBy(() -> category.removeTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 접근입니다.");
    }
}