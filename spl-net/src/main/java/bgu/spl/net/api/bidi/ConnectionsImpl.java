package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.rci.RCIClient;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.NonBlockingConnectionHandler;
import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.util.HashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    HashMap<Integer, ConnectionHandler<T>> map;
    private int index=0;

    public ConnectionsImpl(){
        map = new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if (!map.containsKey(connectionId)) return false;
        int b;
        ConnectionHandler handler = map.get(connectionId);
        handler.send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for (ConnectionHandler handler: map.values())
            handler.send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        if (map.containsKey(connectionId))
            map.remove(connectionId);
    }

    @Override
    public int connect(ConnectionHandler<T> client) {
        map.put(index++, client);
        return index-1;
    }

}
