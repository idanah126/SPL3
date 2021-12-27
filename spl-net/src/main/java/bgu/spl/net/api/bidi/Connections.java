package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.io.IOException;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);

    void connect(int connectionId, NonBlockingConnectionHandler<T> client);
}
