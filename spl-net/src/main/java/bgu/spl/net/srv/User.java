package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.Message;
import bgu.spl.net.srv.messages.PM;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class User {
    private String Username;
    private String password;
    private boolean loggedIn;
    private int age;
    private LinkedList<String> followers;
    private LinkedList<String> following;
    private LinkedList<Message> unnotifiedMessages;
    private LinkedList<String> blockedUsers;
    private LinkedList<String> posts;
    private LinkedList<String> PMs;

    public User(String username, String password, String birthday){
        this.Username=username;
        this.password=password;
        loggedIn=false;
        age= calAge(birthday);
        followers=new LinkedList<>();
        following = new LinkedList<>();
        unnotifiedMessages=new LinkedList<>();
        blockedUsers=new LinkedList<>();
        posts= new LinkedList<>();
        PMs= new LinkedList<>();
    }

    private int calAge(String date){
        //date = DD-MM-YYYY
        int year = Integer.parseInt(date.substring(6,10));
        int month = Integer.parseInt(date.substring(3, 5));
        int day = Integer.parseInt(date.substring(0,2));
        LocalDateTime now= LocalDateTime.now();
        int age = now.getYear() - year;
        if (now.getMonthValue()< month || (now.getMonthValue() == month && now.getDayOfMonth() < day))
            age--;
        return age;
    }

    public boolean isValidUserAndPass(String username, String pass){
        return password.equals(pass) & Username.equals(username);}
    public boolean isLoggedIn(){return loggedIn;}
    public boolean isFollowing(String username){return following.contains(username);}
    public LinkedList<Message> getUnnotifiedMessages(){return unnotifiedMessages;}
    public String getUserName() {return Username;}

    public void LogIn() {
        loggedIn=true;
    }

    public void logOut() {
        loggedIn=false;
    }

    public LinkedList<String> getFollowers() {
        return followers;
    }

    public String getStat() {
        String ans = ""+age+" "+posts.size()+" "+followers.size()+" "+following.size();
        return ans;
    }

    public void Block(String userName) {
        blockedUsers.add(userName);
        unFollow(userName); removeFollower(userName);
    }

    public boolean isBlocking(String username) {
        return blockedUsers.contains(username);
    }

    public void unFollow(String userName) {
        following.remove(userName);
    }

    public void removeFollower(String userName) {
        followers.remove(userName);
    }

    public void Follow(String userName) {
        following.add(userName);
    }

    public void addFollower(String userName) {
        followers.add(userName);
    }

    public void addPost(String content) {
        posts.add(content);
    }

    public void addPM(String content) {
        PMs.add(content);
    }

    public void addToUnnotifiedList(Message msg) {
        unnotifiedMessages.add(msg);
    }
}
