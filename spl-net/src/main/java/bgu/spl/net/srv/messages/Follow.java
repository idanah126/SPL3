package bgu.spl.net.srv.messages;

public class Follow extends Message{

    public final boolean unFollow;
    public final String userName;

    public Follow(boolean unFollow ,String userName){
        this.unFollow = unFollow;
        this.userName = userName;
    }
}
