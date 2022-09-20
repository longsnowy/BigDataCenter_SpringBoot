package snow.utils;

import java.util.HashMap;
import java.util.Map;

//Response封装工具类
public class ApiResponse {
    public static Map<String,Object> toJson(Integer code, Map<String,Object> m) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",code);
        map.put("data",m);
        return map;
    }
    public static Map<String,Object> toJsonSuccess(Integer code, String s) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",code);
        map.put("data",s);
        return map;
    }
    public static Map<String,Object> toJsonDefault(Integer code, String s) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",code);
        map.put("message",s);
        return map;
    }
}
