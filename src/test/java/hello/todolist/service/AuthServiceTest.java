package hello.todolist.service;

import hello.todolist.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired private AuthService service;

    @Test
    void 정상_회원가입() {
        // given
        String loginId = "testId";
        String password = "testPassword";

        // when
        User savedUser = service.join(loginId, password);

        // then
        assertNotNull(savedUser);
        assertEquals(loginId, savedUser.getLoginId());
        assertTrue(PasswordUtil.verifyPassword(password, savedUser.getPassword()));
    }

    @Test
    void 가입된_아이디로_재가입() {
        // given
        String loginId = "testId";
        String password = "testPassword";
        User savedUser = service.join(loginId, password);

        // when & then
        assertThatThrownBy(() -> service.join(loginId, password))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 회원입니다");
    }

    @Test
    void 정상_로그인() {
        // given
        String loginId = "testId";
        String password = "testPassword";
        User savedUser = service.join(loginId, password);

        // when
        User loginUser = service.login(loginId, password);

        // then
        assertThat(savedUser.getLoginId()).isEqualTo(loginUser.getLoginId());
        assertThat(savedUser.getPassword()).isEqualTo(loginUser.getPassword());
    }

    @Test
    void 잘못된_패스워드로_로그인() {
        // given
        String loginId = "testId";
        String password = "testPassword";
        User savedUser = service.join(loginId, password);

        String wrongPassword = "wrongPassword";

        // when
        User loginUser = service.login(loginId, wrongPassword);

        // then
        assertThat(loginUser).isNull();
    }

    @Test
    void 로그인ID로_사용자_찾기() {
        // given
        String loginId = "testId";
        String password = "testPassword";
        User savedUser = service.join(loginId, password);

        // when
        User findUser = service.findUserByLoginId(loginId);

        // then
        assertThat(savedUser.getLoginId()).isEqualTo(findUser.getLoginId());
        assertThat(savedUser.getPassword()).isEqualTo(findUser.getPassword());
    }

    @Test
    void 가입되지_않은_사용자_찾기() {
        // given
        String loginId = "testId";

        // when & then
        assertThatThrownBy(() -> service.findUserByLoginId(loginId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 회원입니다");
    }
}