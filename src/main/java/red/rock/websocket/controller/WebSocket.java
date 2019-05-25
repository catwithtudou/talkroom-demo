package red.rock.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import red.rock.websocket.config.MyApplicationContextAware;
import red.rock.websocket.entity.Friend;
import red.rock.websocket.entity.User;
import red.rock.websocket.mapper.UserMapper;
import red.rock.websocket.service.UserService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/5/23 19:34
 **/

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket{

    protected UserService userService=(UserService) MyApplicationContextAware.getApplicationContext().getBean("userService");

    /**
     * 在线人数
     */
    private static int onlineNumber=0;

    /**
     * 以用户的姓名为key,WebSocket为对象保存起来
     */
    private static Map<String,WebSocket> clients=new ConcurrentHashMap<>();

    /**
     * 会话
     */
    private Session session;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;



    /**
     * 建立连接
     * @param session
     * @return
     */
    @OnOpen
    public void onOpen(@PathParam("username")String username,Session session){
        onlineNumber++;
        this.username=username;
        this.session=session;
        User user=userService.getSelf(username);
        String nickname=user.getNickname();
        this.nickname=nickname;
        try{
            //messageType 1 代表上线 2 代表下线 3 代表在线名单 4 代表普通信息
            //第一步登陆后通知本人上线了
            Map<String,Object> map1= Maps.newHashMap();
            map1.put("messageType",1);
            map1.put("nickname",nickname);
            sendMessageAll(JSON.toJSONString(map1));
            System.out.println(nickname);
            //将自己的信息存入map中
            clients.put(nickname,this);

            //给自己发一条消息:告诉自己现在都有谁在线
            Map<String,Object> map2=Maps.newHashMap();
            map2.put("messageType",3);
            map2.put("nickname",nickname);
            //移除掉自己
            Set<String> set=clients.keySet();
            map2.put("onlineUsers",set);
            sendMessageTo(JSON.toJSONString(map2),nickname);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @OnError
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }

    @OnClose
    public void onClose(){
        onlineNumber--;
        clients.remove(nickname);
        clients.remove(username);
        try{
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> map1=Maps.newHashMap();
            map1.put("messageType",2);
            map1.put("onlineUsers",clients.keySet());
            map1.put("username",username);
            map1.put("nickname",nickname);
            sendMessageAll(JSON.toJSONString(map1));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端的消息
     * @param message
     * @param session
     * @return void
     */
    @OnMessage
     public void onMessage(String message,Session session){
        try{
            User user=userService.getSelf(username);
            JSONObject jsonObject=JSON.parseObject(message);
            String textMessage=jsonObject.getString("message");
            String fromUsername=jsonObject.getString("username");
            String fromNickname=user.getNickname();
            String toUsername=jsonObject.getString("to");
            System.out.println("username="+username);
            System.out.println("text="+textMessage);
            System.out.println("to="+toUsername);
            //若不是发给所有人,那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> map1=Maps.newHashMap();
            map1.put("messageType",4);
            map1.put("textMessage",textMessage);
            map1.put("fromUsername",fromUsername);
            map1.put("fromNickname",fromNickname);
            if(toUsername.equals("ALL")){
                map1.put("toNickname","所有人");
                sendMessageAll(JSON.toJSONString(map1));
            }else{
                List<Friend> friends=userService.getFriends(fromUsername);
                boolean flag=false;
                for(Friend friend:friends){
                    if(friend.getFriendNickname().equals(toUsername)){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    map1.put("toNickname",toUsername);
                    sendMessageTo(JSON.toJSONString(map1),toUsername);
                }else{
                    map1.put("toNickname","失败");
                    sendMessageTo(JSON.toJSONString(map1),fromNickname);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sendMessageTo(String message,String toNickname)throws IOException{
        for(WebSocket item:clients.values()){
            if(item.nickname.equals(toNickname)){
                item.session.getAsyncRemote().sendText(message);
            }
        }
    }


    public void sendMessageAll(String message)throws IOException{
        for(WebSocket item:clients.values()){
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized  int getOnlineNumber(){
        return onlineNumber;
    }

}
