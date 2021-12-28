package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.messages.*;
import bgu.spl.net.srv.messages.Error;
import sun.security.jca.GetInstance;

import java.awt.*;
import java.util.LinkedList;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate=false;
    private Connections connections;
    private int connectionID;
    private Data data;

    public BidiMessagingProtocolImpl(){
        connections=null;
        connectionID=-1;
        data=null;
    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections=connections;
        this.connectionID=connectionId;
        data=Data.getData();
    }

    @Override
    public void process(Message message) {
        if (message instanceof Block) processBlock((Block)message);
        if (message instanceof Follow) processFollow((Follow)message);
        if (message instanceof Login) processLogin((Login)message);
        if (message instanceof Logout) processLogout((Logout)message);
        if (message instanceof Logstat) processLogstat((Logstat)message);
        if (message instanceof PM) processPM((PM)message);
        if (message instanceof Post) processPost((Post)message);
        if (message instanceof Register) processRegister((Register)message);
        if (message instanceof Stat) processStat((Stat)message);
    }

    private void processRegister(Register message) {
        if (data.isRegistered(connectionID))
            connections.send(connectionID, new Error(1));
        else{
            data.register(message, connectionID);
            connections.send(connectionID, new Ack(1));
        }
    }

    private void processLogin(Login message) {
        if (!data.isRegistered(connectionID) | !data.isUserAndPassCorrect(connectionID, message.userName, message.password)
                | data.isLoggedIn(connectionID) | message.captcha==false)
            connections.send(connectionID,new Error(2));
        else {
            data.login(connectionID);
            connections.send(connectionID, new Ack(2));
        }
    }

    private void processLogout(Logout message) {
        if (!data.isLoggedIn(connectionID))
            connections.send(connectionID, new Error(3));
        else{
            data.logout(connectionID);
            connections.send(connectionID, new Ack(3));
        }
    }

    private void processFollow(Follow message) {
        if (message.unFollow){
            if (!data.isLoggedIn(connectionID) | data.isFollowing(connectionID, message.userName))
                connections.send(connectionID, new Error(4));
            else {
                data.unfollow(connectionID, message.userName);
                connections.send(connectionID, new Ack(4, message.userName));
            }
        }
        else{
            if (!data.isLoggedIn(connectionID) | !data.isFollowing(connectionID, message.userName)
                    |data.isBlockedby(connectionID, message.userName))
                connections.send(connectionID, new Error(4));
            else {
                data.follow(connectionID, message.userName);
                connections.send(connectionID, new Ack(4, message.userName));
            }
        }
    }

    private void processPost(Post message) {
        if (!data.isLoggedIn(connectionID))
            connections.send(connectionID, new Error(5));
        else {
            data.post(connectionID, message.content);
            LinkedList<Integer> users = data.getFollowersId(connectionID);
            String post = message.content;
            int indexOfUsername = post.indexOf('@');
            while (indexOfUsername != -1) {
                post = post.substring(indexOfUsername + 1);
                int endOfUsername = post.indexOf(" ");
                users.add(data.getIdByUsername(post.substring(0, endOfUsername)));
                indexOfUsername = post.indexOf('@');
            }
            for (Integer connId : users) {
                connections.send(connId, new Notification(true, data.getUsernameById(connectionID), message.content));
            }
        }
    }

    private void processPM(PM message) {
        if (!data.isLoggedIn(connectionID) | !data.isRegistered(message.userName)
                | !data.isFollowing(connectionID, message.userName))
            connections.send(connectionID, new Error(6));
        else{
            String content = data.filter(message.content);
            data.PM(connectionID,content);
            connections.send(data.getIdByUsername(message.userName), new Notification(false, data.getUsernameById(connectionID), content));
        }
    }

    private void processLogstat(Logstat message) {
        if (!data.isLoggedIn(connectionID))
            connections.send(connectionID, new Error(7));
        else {
            LinkedList<String> ans = data.getLoggedInUserStat(connectionID);
            for (String s: ans)
                connections.send(connectionID, new Ack(7, s));
        }
    }

    private void processStat(Stat message) {
        if (!data.isLoggedIn(connectionID))
            connections.send(connectionID, new Error(7));
        else {
            for (String s: message.usernames){
                if (!data.isRegistered(s) | data.isBlockedby(connectionID, s)){
                    connections.send(connectionID, new Error(8));
                    break;
                }
                else {
                    String stat = data.getStat(s);
                    connections.send(connectionID, new Ack(8, s));
                }
            }
        }
    }

    private void processBlock(Block message) {
        if (!data.isRegistered(message.userName))
            connections.send(connectionID, new Error(12));
        else{
            data.Block(connectionID, message.userName);
            connections.send(connectionID, new Ack(12));
        }
    }



    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
