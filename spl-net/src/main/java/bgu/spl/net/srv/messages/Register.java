package bgu.spl.net.srv.messages;

public class Register extends Message{

    private String userName;
    private String password;
    private String birthday;

    public Register(String userName, String password, String birthday){
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }
}
