package lk.ijse.therapycenter.util;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^(\\+94|0)[0-9]{9}$");
    private static final Pattern NAME  = Pattern.compile("^[A-Za-z ]{2,100}$");
    private static final Pattern UNAME = Pattern.compile("^[A-Za-z0-9_]{4,30}$");
    private static final Pattern PASS  = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");



    public static boolean isValidEmail(String v) {
        return v!=null && EMAIL.matcher(v).matches();
    }

    public static boolean isValidPhone(String v){
        return v!=null && PHONE.matcher(v).matches();
    }

    public static boolean isValidName(String v){
        return v!=null && NAME.matcher(v.trim()).matches();
    }

    public static boolean isValidUsername(String v) {
        return v!=null && UNAME.matcher(v).matches();
    }

    public static boolean isValidPassword(String v) {
        return v!=null && PASS.matcher(v).matches();
    }

    public static boolean isNotEmpty(String v){
        return v!=null && !v.trim().isEmpty();
    }



}
