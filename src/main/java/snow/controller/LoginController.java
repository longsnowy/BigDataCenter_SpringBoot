package snow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import snow.entity.User;
import snow.service.DataSourceService;
import snow.service.UserService;
import snow.utils.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    //登录验证的接口
    @ResponseBody
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {


        String a = userService.login(user);
        if (a == "success") {
            return ApiResponse.toJsonSuccess(20000, user.toString());
        } else {
            return ApiResponse.toJsonDefault(60204, a);
        }
    }

    @RequestMapping("/loginout")
    @ResponseBody
    public Map<String, Object> out() {
        return ApiResponse.toJsonSuccess(20000, "退出成功");
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "<h1>AAAAA</h1>";
    }



    @RequestMapping("/info")
    @ResponseBody
    public Map<String,Object> info(String token) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name","admin");
        String[] roles = {"admin"};
        map.put("roles",roles);
        map.put("introduction","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ApiResponse.toJson(20000,map);
    }
}
