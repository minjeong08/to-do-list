package hello.todolist.controller.category;

import hello.todolist.domain.Category;
import hello.todolist.domain.User;
import hello.todolist.service.CategoryService;
import hello.todolist.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;

    @GetMapping
    public String categoryInfo(Model model, HttpSession session) {
        List<Category> categories = authService.getCategories(session.getAttribute("loginUser").toString());

        model.addAttribute("categories", categories);
        return "category/category";
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody Map<String, String> request, HttpSession session) {
        String cateName = request.get("cateName");
        Map<String, Object> response = new HashMap<>();

        try {
            categoryService.createCategory(cateName, session.getAttribute("loginUser").toString());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{cateId}")
    public ResponseEntity<Map<String, Object>> updateCateName(@PathVariable Long cateId, @RequestBody Map<String, String> request, HttpSession session) {
        String newCateName = request.get("cateName");
        Map<String, Object> response = new HashMap<>();

        if (newCateName == null || newCateName.isEmpty()) {
            response.put("success", false);
            response.put("message", "카테고리 이름이 유효하지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Category findCategory = categoryService.findCategoryById(cateId);

        if (!getLoginUser(session).equals(findCategory.getUser())) {
            response.put("success", false);
            response.put("message", "카테고리를 삭제할 권한이 없습니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        categoryService.updateCateName(findCategory, newCateName);
        response.put("success", true);
        response.put("message", "카테고리를 삭제 성공");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete/{cateId}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long cateId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Category findCategory = categoryService.findCategoryById(cateId);

        if (!getLoginUser(session).equals(findCategory.getUser())) {
            response.put("success", false);
            response.put("message", "카테고리를 삭제할 권한이 없습니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        categoryService.deleteCategory(findCategory);
        response.put("success", true);
        response.put("message", "카테고리를 삭제 성공");
        return ResponseEntity.ok(response);
    }

    private User getLoginUser(HttpSession session) {
        String loginId = (String) session.getAttribute("loginUser");

        if (loginId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        return authService.findUserByLoginId(loginId);
    }
}
