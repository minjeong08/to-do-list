package hello.todolist.repository;

import hello.todolist.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCateName(String cateName);
}
