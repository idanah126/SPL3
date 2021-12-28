package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.messages.*;
import bgu.spl.net.srv.messages.Error;
import sun.security.jca.GetInstance;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate=false;
    private Connections connections;
    private int connectionID;

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections=connections;
        this.connectionID=connectionId;
    }

    @Override
    public void process(Message message) {
        if (message instanceof Ack) processAck(message);
        if (message instanceof Block) processBlock(message);
        if (message instanceof Error) processError(message);
        if (message instanceof Follow) processFollow(message);
        if (message instanceof Login) processLogin(message);
        if (message instanceof Logout) processLogout(message);
        if (message instanceof Logstat) processLogstat(message);
        if (message instanceof Notification) processNotification(message);
        if (message instanceof PM) processPM(message);
        if (message instanceof Post) processPost(message);
        if (message instanceof Register) processRegister((Register)message);
        if (message instanceof Stat) processStat(message);


    }

    private void processRegister(Register message) {

    }

    private void processStat(Message message) {
    }

    private void processPost(Message message) {
    }

    private void processPM(Message message) {
    }

    private void processNotification(Message message) {
    }

    private void processLogstat(Message message) {
    }

    private void processLogout(Message message) {
    }

    private void processLogin(Message message) {
    }

    private void processFollow(Message message) {
    }

    private void processBlock(Message message) {
    }

    private void processError(Message message) {
    }

    private void processAck(Message message) {
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
