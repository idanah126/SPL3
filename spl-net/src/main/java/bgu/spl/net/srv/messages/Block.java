package bgu.spl.net.srv.messages;

public class Block extends Message {

    public final String userName;

    public Block(String userName){
        this.userName = userName;
    }
}
