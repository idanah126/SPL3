package bgu.spl.net.srv.messages;

public class Error extends Message{

    public final int msgOpcode;

    public Error(int msgOpcode){
        this.msgOpcode = msgOpcode;
    }
}
