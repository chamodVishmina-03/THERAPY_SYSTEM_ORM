package lk.ijse.therapycenter.util;
import org.mindrot.jbcrypt.BCrypt;



public class PasswordUtil {

    public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }


    public static boolean verifyPassword(String plain, String hashed)  {
        return BCrypt.checkpw(plain, hashed);
    }
}
