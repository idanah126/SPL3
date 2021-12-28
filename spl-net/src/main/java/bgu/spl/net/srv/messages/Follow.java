package bgu.spl.net.srv.messages;

public class Follow extends Message{

    private boolean unFollow;
    private String userName;

    public Follow(boolean unFollow ,String userName){
        this.unFollow = unFollow;
        this.userName = userName;
    }
}
