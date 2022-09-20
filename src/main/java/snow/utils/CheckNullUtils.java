package snow.utils;

public class CheckNullUtils {
    public static String checkNull(String checkStr){
        if (checkStr.equals("")){
            return null;
        }
        else {
            return checkStr;
        }
    }
}
