package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.Message;
import com.sun.tools.javac.util.List;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class User {
    private String Username;
    private String password;
    private boolean loggedIn;
    private int age;
    private LinkedList<Integer> followers;
    private LinkedList<Integer> following;
    private int numOfPosts;
    private LinkedList<Message> unnotifiedMessages;

    public User(String username, String password, String birthday){
        this.Username=username;
        this.password=password;
        loggedIn=false;
        age= calAge(birthday);
        followers=new LinkedList<>();
        following = new LinkedList<>();
        numOfPosts=0;
        unnotifiedMessages=new LinkedList<>();
    }

    private int calAge(String date){
        //date = DD-MM-YYYY
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        LocalDateTime now= LocalDateTime.now();
        int age = now.getYear() - year;
        if (now.getMonthValue()< month || (now.getMonthValue() == month && now.getDayOfMonth() < day))
            age--;
        return age;
    }

    public boolean isValidPass(String pass){ return password.equals(pass);}
    public boolean isLoggedIn(){return isLoggedIn();}
    public boolean isFollowing(int connID){return following.contains(connID);}
    public LinkedList<Message> getUnnotifiedMessages(){return unnotifiedMessages;}
    public String getUserName() {return Username;}
}
