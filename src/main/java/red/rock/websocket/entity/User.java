package red.rock.websocket.entity;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 18:40
 **/

@Data
public class User {

    private String username;
    private String password;
    private String nickname;
    private List<Friend> friends;

    public User(){

    }

    public User(String username,String password,String nickname){
        this.username=username;
        this.nickname=nickname;
        this.password=password;
    }

}
