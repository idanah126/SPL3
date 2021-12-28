package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.rci.RCIClient;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.NonBlockingConnectionHandler;
import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.util.HashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    HashMap<Integer, BlockingConnectionHandler<T>> map;
    private int index=0;

    public ConnectionsImpl(){
        map = new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if (!map.containsKey(connectionId)) return false;
        int b;
        BlockingConnectionHandler handler = map.get(connectionId);
        handler.send(T msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        //need to use connectionHandler
        //for each value in map, value.recieveMessage(msg)
    }

    @Override
    public void disconnect(int connectionId) {
        //map.remove(connId)
    }

    @Override
    public int connect(BlockingConnectionHandler<T> client) {
        map.put(index++, client);
        return index-1;
    }

}
