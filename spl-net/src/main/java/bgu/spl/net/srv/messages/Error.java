package bgu.spl.net.srv.messages;

public class Error extends Message{

    private int msgOpcode;

    public Error(int msgOpcode){
        this.msgOpcode = msgOpcode;
    }
}
