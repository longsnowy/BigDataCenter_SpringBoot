package snow.service;

import snow.entity.User;
import snow.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService{

    @Autowired
    private UserMapper userMapper;

    //登录验证
    public String login(User user) {
        User userAuth = userMapper.selectById(user.getUsername());
        System.out.println(user);
        System.out.println(userAuth);
        if(user == null)
            return "name error";
        if(!user.getPassword().equals(userAuth.getPassword()))
            return "password error";
        return "success";
    }
}
