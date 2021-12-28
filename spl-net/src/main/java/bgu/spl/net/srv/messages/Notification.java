package bgu.spl.net.srv.messages;

public class Notification extends Message {

    public final boolean isPublic;
    public final String postingUser;
    public final String content;

    public Notification(boolean isPublic, String postingUser, String content){
        this.isPublic = isPublic;
        this.postingUser = postingUser;
        this.content = content;
    }
}
