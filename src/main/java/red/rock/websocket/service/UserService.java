package red.rock.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import red.rock.websocket.entity.Friend;
import red.rock.websocket.entity.User;
import red.rock.websocket.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 19:13
 **/

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     * @param user
     * @return boolean
     */
    public boolean registerUser(User user){
        User user1=userMapper.getUser(user.getUsername());
        boolean flag=false;
        if(user1!=null){
            flag=false;
        }
        else if(userMapper.addUser(user)){
            flag=true;
        }
        return flag;
    }

    /**
     * 登录服务
     * @param  username
     * @param  password
     * @return  boolean
     */
    public boolean login(String username,String password){
        if(username==null||password==null||"".equals(username)||"".equals(password)){
            return false;
        }
        boolean flag=false;
        User user=userMapper.getUser(username);
        if(user!=null){
            String password1=user.getPassword();
            if(password.equals(password1)){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 添加好友服务
     * @param username
     * @param friendName
     * @return boolean
     */
    public boolean addFriend(String username,String friendName){
        boolean flag=false;
        User user=userMapper.getUser(friendName);
        if(user!=null){
            String nickname=user.getNickname();
            Friend friend=new Friend(username,friendName,nickname);
            if(userMapper.addUserFriend(friend)){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 查询用户好友列表
     * @param username
     * @return List<Friend>
     */
    public List<Friend> getFriends(String username){
        List<Friend> friends=new ArrayList<>();
        friends=userMapper.getUserFriends(username);
        return friends;
    }

    /**
     * 获取自身信息
     * @param
     * @return
     */
    public User getSelf(String username){
        User user=userMapper.getUser(username);
        return user;
    }
}
