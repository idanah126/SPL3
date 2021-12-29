package bgu.spl.net.srv.messages;

public class Ack extends Message{

    public final int messageOpcode;
    public final String optional;

    public Ack(int messageOpcode){
        this.messageOpcode = messageOpcode;
        optional = null;
    }

    public Ack(int messageOpcode, String optional){
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }
}
