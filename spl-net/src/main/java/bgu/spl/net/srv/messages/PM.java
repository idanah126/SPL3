package bgu.spl.net.srv.messages;

public class PM extends Message{

    public final String userName;
    public final String content;
    public final String dateAndTime;

    public PM(String userName, String content, String dateAndTime){
        this.userName = userName;
        this.content = content;
        this.dateAndTime = dateAndTime;
    }
}
