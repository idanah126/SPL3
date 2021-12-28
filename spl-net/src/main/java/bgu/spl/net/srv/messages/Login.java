package bgu.spl.net.srv.messages;

public class Login extends Message{

    public final String userName;
    public final String password;
    public final boolean captcha;

    public Login(String userName, String password, boolean captcha){
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }
}
