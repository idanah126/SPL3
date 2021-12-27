package bgu.spl.net.srv.messages;

public class Ack extends Message{

    private String optional;

    public Ack(){
        optional = null;
    }

    public Ack(String optional){
        this.optional = optional;
    }
}
