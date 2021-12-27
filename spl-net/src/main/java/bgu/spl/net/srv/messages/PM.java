package bgu.spl.net.srv.messages;

public class PM extends Message{

    private String userName;
    private String content;
    private String dateAndTime;

    public PM(String userName, String content, String dateAndTime){
        this.userName = userName;
        this.content = content;
        this.dateAndTime = dateAndTime;
    }
}
