package red.rock.websocket.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import red.rock.websocket.entity.Friend;
import red.rock.websocket.entity.User;

import java.util.List;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 18:43
 **/

@Repository
@Mapper
public interface UserMapper {

    /**
     * 添加用户
     * @param user
     * @return boolean
     */
    @Insert("insert into user(username,password,nickname) values(#{username},#{password},#{nickname})")
    boolean addUser(User user);

    /**
     * 查询用户信息不包括好友列表
     * @param username
     * @return User
     */
    @Select("select * from user where username=#{username}")
    @Results({
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "nickname",column = "nickname")
    })
    User getUser(String username);

    /**
     * 查询用户好友列表
     * @param  username
     * @return List<Friend>
     */
    @Select("select * from friend where username=#{username}")
    @Results({
            @Result(property = "friendUsername",column = "friend_username"),
            @Result(property = "friendNickname",column = "friend_nickname")
    })
    List<Friend> getUserFriends(String username);

    /**
     * 添加好友
     * @param friend
     * @return boolean
     */
    @Insert("insert into friend(username,friend_username,friend_nickname) values(#{username},#{friendUsername},#{friendNickname})")
    boolean addUserFriend(Friend friend);
}
