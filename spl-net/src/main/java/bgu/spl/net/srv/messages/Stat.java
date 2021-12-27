package bgu.spl.net.srv.messages;

import java.util.List;

public class Stat extends Message{

    private List<String> usernames;

    public Stat(List<String> usernames){
        this.usernames = usernames;
    }
}
