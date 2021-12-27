package bgu.spl.net.srv.messages;

public class Notification extends Message {

    private boolean isPublic;
    private String postingUser;
    private String content;

    public Notification(boolean isPublic, String postingUser, String content){
        this.isPublic = isPublic;
        this.postingUser = postingUser;
        this.content = content;
    }
}
