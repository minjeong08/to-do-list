package hello.todolist.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hasPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
