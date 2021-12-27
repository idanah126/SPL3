package bgu.spl.net.srv.messages;

public class Login extends Message{

    private String userName;
    private String password;
    private boolean captcha;

    public Login(String userName, String password, boolean captcha){
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }
}
