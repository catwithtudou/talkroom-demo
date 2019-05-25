package red.rock.websocket.entity;

import lombok.Data;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 19:04
 **/
@Data
public class Friend {

    private String username;
    private String friendUsername;
    private String friendNickname;

    public Friend(){

    }

    public Friend(String username,String friendUsername,String friendNickname){
        this.username=username;
        this.friendNickname=friendNickname;
        this.friendUsername=friendUsername;
    }
}
