package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.messages.*;
import bgu.spl.net.srv.messages.Error;
import sun.security.jca.GetInstance;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate=false;
    private Connections connections;
    private int connectionID;
    private Data data;

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections=connections;
        this.connectionID=connectionId;
        data=Data.getData();
    }

    @Override
    public void process(Message message) {
        if (message instanceof Ack) processAck((Ack)message);
        if (message instanceof Block) processBlock((Block)message);
        if (message instanceof Error) processError((Error)message);
        if (message instanceof Follow) processFollow((Follow)message);
        if (message instanceof Login) processLogin((Login)message);
        if (message instanceof Logout) processLogout((Logout)message);
        if (message instanceof Logstat) processLogstat((Logstat)message);
        if (message instanceof Notification) processNotification((Notification)message);
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
        else
            data.login(connectionID);
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
    }

    private void processPost(Post message) {
    }

    private void processPM(PM message) {
    }

    private void processLogstat(Logstat message) {
    }

    private void processStat(Stat message) {
    }

    private void processNotification(Notification message) {
    }

    private void processAck(Ack message) {
    }

    private void processBlock(Block message) {
    }

    private void processError(Error message) {
    }


    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
