package bgu.spl.net.srv.messages;

public class Post extends Message{

    public final String content;

    public Post(String content){
        this.content = content;
    }
}
