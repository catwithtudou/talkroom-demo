package red.rock.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import red.rock.websocket.entity.User;
import red.rock.websocket.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 23:39
 **/

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void login(@RequestParam(name = "username",required = false)String username, @RequestParam(name = "password",required = false)String password, HttpSession session,
                      HttpServletResponse response)throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");
        if(username.equals("")||password.equals("")){
             response.getWriter().write("请输入账号或者密码");
        }
        boolean flag=userService.login(username,password);
        if(!flag){
            response.getWriter().print("<script>alert('用户名或密码错误');history.back();</script>");
            }else {
            session.setAttribute("user", username);
            response.getWriter().print("<script>alert('用户登陆成功');window.location='/websocket/" + username + "';</script>");
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@RequestParam("username")String username,@RequestParam("password")String password,@RequestParam("nickname")String nickname){
        if(username.equals("")||password.equals("")||nickname.equals("")){
            return "请输入相关信息";
        }
        User user=new User(username,password,nickname);
        boolean flag=userService.registerUser(user);
        if(!flag){
            return "用户名重复,请重新输入";
        }
        return "注册成功";
    }

    @RequestMapping(value = "/addFriend",method = RequestMethod.POST)
    public String addFriend(@RequestParam("friendUsername")String friendUsername,HttpSession session){
        String username= (String) session.getAttribute("user");
        if(username==null){
            return "请先登录账号";
        }
        boolean flag=userService.addFriend(username,friendUsername);
        if(!flag){
            return "添加好友失败,请检查是否存在此人";
        }
        return "添加好友成功";
    }
}
