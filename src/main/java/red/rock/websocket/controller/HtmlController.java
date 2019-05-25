package red.rock.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 23:52
 **/

@Controller
public class HtmlController {

    @GetMapping("/login")
    public String login(){
        return "/login.html";
    }

    @GetMapping("/register")
    public String register(){
        return "/register.html";
    }

    @GetMapping("/addFriend")
    public String addFriend(){
        return "/addFriend.html";
    }

    @RequestMapping("/websocket/{username}")
    public String websocket(@PathVariable String username, Model model){
        try{
            model.addAttribute("username",username);
            return "websocket";
        }catch (Exception e){
            return "error";
        }
    }


}
