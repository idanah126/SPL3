package bgu.spl.net.srv.messages;

public class Register extends Message{

    public final String userName;
    public final String password;
    public final String birthday;

    public Register(String userName, String password, String birthday){
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }
}
