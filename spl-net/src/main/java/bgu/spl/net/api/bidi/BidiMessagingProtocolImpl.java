package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.messages.*;
import bgu.spl.net.srv.messages.Error;

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
        if (data.isRegistered(connectionID) | data.isRegistered(message.userName))
            send(connectionID, new Error(1));
        else{
            data.register(message, connectionID);
            send(connectionID, new Ack(1));
        }
    }

    private void processLogin(Login message) {
        if (!data.isRegistered(connectionID) || !data.isUserAndPassCorrect(connectionID, message.userName, message.password)
                | data.isLoggedIn(connectionID) | !message.captcha)
            send(connectionID,new Error(2));
        else {
            data.login(connectionID);
            send(connectionID, new Ack(2));
            LinkedList<Message> unnotifiedMsg = data.getUnnotifiedMessages(connectionID);
            for (Message msg: unnotifiedMsg)
                send(connectionID, msg);
        }
    }

    private void processLogout(Logout message) {
        if (!data.isLoggedIn(connectionID))
            send(connectionID, new Error(3));
        else{
            data.logout(connectionID);
            send(connectionID, new Ack(3));
            shouldTerminate=true;
        }
    }

    private void processFollow(Follow message) {
        if (message.userName==data.getUsernameById(connectionID))
            send(connectionID, new Error(4));
        else if (message.unFollow){
            if (!data.isLoggedIn(connectionID) || !data.isRegistered(message.userName) || !data.isFollowing(connectionID, message.userName))
                send(connectionID, new Error(4));
            else {
                data.unfollow(connectionID, message.userName);
                send(connectionID, new Ack(4, message.userName));
            }
        }
        else{
            if (!data.isLoggedIn(connectionID) || !data.isRegistered(message.userName) || data.isFollowing(connectionID, message.userName)
                    | data.isBlockedby(connectionID, message.userName) | data.isBlockedby(message.userName, connectionID))
                send(connectionID, new Error(4));
            else {
                data.follow(connectionID, message.userName);
                send(connectionID, new Ack(4, message.userName));
            }
        }
    }

    private void processPost(Post message) {
        if ( !data.isLoggedIn(connectionID))
            send(connectionID, new Error(5));
        else {
            LinkedList<Integer> users = data.getFollowersId(connectionID);
            LinkedList<Integer> mentioned = new LinkedList<>();
            String post = message.content;
            int indexOfUsername = post.indexOf('@');
            while (indexOfUsername != -1) {
                post = post.substring(indexOfUsername + 1);
                int endOfUsername = post.indexOf(" ");
                if (endOfUsername==-1) endOfUsername=post.length();
                if(!data.isRegistered(post.substring(0, endOfUsername))) {
                    indexOfUsername = post.indexOf('@');
                    continue;
                }
                if (!users.contains(data.getIdByUsername(post.substring(0, endOfUsername))))
                    mentioned.add(data.getIdByUsername(post.substring(0, endOfUsername)));
                indexOfUsername = post.indexOf('@');
            }
            data.post(connectionID, message.content);
            send(connectionID, new Ack(5));
            for (Integer connId : users) {
                send(connId, new Notification(true, data.getUsernameById(connectionID), message.content));
            }
            for (Integer connId : mentioned) {
                if (!(data.isBlockedby(connectionID, data.getUsernameById(connId)) || data.isBlockedby(connId, data.getUsernameById(connectionID))))
                    send(connId, new Notification(true, data.getUsernameById(connectionID), message.content));
            }
        }
    }

    private void processPM(PM message) {
        if (message.userName==data.getUsernameById(connectionID))
            send(connectionID, new Error(4));
       else  if ( !data.isRegistered(message.userName) || !data.isLoggedIn(connectionID)
                | !data.isFollowing(connectionID, message.userName))
            send(connectionID, new Error(6));
        else{
            String content = data.filter(message.content);
            data.PM(connectionID,content);
            send(connectionID, new Ack(6));
            send(data.getIdByUsername(message.userName), new Notification(false, data.getUsernameById(connectionID), content+" "+message.dateAndTime));
        }
    }

    private void processLogstat(Logstat message) {
        if (!data.isLoggedIn(connectionID))
            send(connectionID, new Error(7));
        else {
            LinkedList<String> ans = data.getLoggedInUserStat(connectionID);
            for (String s: ans)
                send(connectionID, new Ack(7, s));
        }
    }

    private void processStat(Stat message) {
        if (!data.isRegistered(connectionID) || !data.isLoggedIn(connectionID))
            send(connectionID, new Error(8));
        else {
            for (String s: message.usernames){
                if (!data.isRegistered(s) || data.isBlockedby(connectionID, s)| data.isBlockedby(s, connectionID)){
                    send(connectionID, new Error(8));
                    break;
                }
                else {
                    String stat = data.getStat(s);
                    send(connectionID, new Ack(8, stat));
                }
            }
        }
    }

    private void processBlock(Block message) {
        if (!data.isRegistered(connectionID) || !data.isLoggedIn(connectionID) | message.userName==data.getUsernameById(connectionID))
            send(connectionID, new Error(4));
        if (!data.isRegistered(message.userName))
            send(connectionID, new Error(12));
        else{
            data.Block(connectionID, message.userName);
            send(connectionID, new Ack(12));
        }
    }

    private void send(int connId, Message msg){
        if (!data.isLoggedIn(connId) & !((msg instanceof Ack) && (((Ack)msg).messageOpcode==1) | ((Ack)msg).messageOpcode==3) &  !((msg instanceof Error)))
            data.addToUnotifiedList(connId, msg);
        else connections.send(connId, msg);
    }


    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
