package bgu.spl.net.srv.messages;

import java.util.List;

public class Stat extends Message{

    public final List<String> usernames;

    public Stat(List<String> usernames){
        this.usernames = usernames;
    }
}
