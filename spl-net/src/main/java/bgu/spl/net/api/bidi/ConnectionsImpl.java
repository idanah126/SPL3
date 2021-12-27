package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.rci.RCIClient;
import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.util.HashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    HashMap<Integer, NonBlockingConnectionHandler<T>> map;

    public ConnectionsImpl(){
        map = new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        //map[conid].recieveMesange(msg);
        //need to use connectionHandler
        return false;
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
    public void connect(int connectionId, NonBlockingConnectionHandler<T> client) {
        //map.put(connId, client);
    }
}
