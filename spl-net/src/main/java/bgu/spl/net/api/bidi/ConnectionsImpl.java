package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.rci.RCIClient;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.NonBlockingConnectionHandler;
import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.util.HashMap;

public class ConnectionsImpl implements Connections<Message> {

    HashMap<Integer, BlockingConnectionHandler<Message>> map;
    private HashMap<Integer, User> users;
    private int index=0;

    public ConnectionsImpl(){
        users=new HashMap<>();
        map = new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, Message msg) {
        //map[conid].recieveMesange(msg);
        //need to use connectionHandler
        return false;
    }

    @Override
    public void broadcast(Message msg) {
        //need to use connectionHandler
        //for each value in map, value.recieveMessage(msg)
    }

    @Override
    public void disconnect(int connectionId) {
        //map.remove(connId)
    }

    @Override
    public int connect(BlockingConnectionHandler<Message> client) {
        map.put(index++, client);
        return index-1;
    }


    public void AddToUsers(User u){
        users.put(users.size(), u);
    }
    public Boolean isRegistered(String username){
        for (User u: users.values())
            if (u.getUserName().equals(username))
                return true;
        return false;
    }
}
