package bgu.spl.net.srv.messages;

public class Follow extends Message{

    private boolean follow;
    private String userName;

    public Follow(boolean follow ,String userName){
        this.follow = follow;
        this.userName = userName;
    }
}
