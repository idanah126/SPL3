package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;
import bgu.spl.net.srv.messages.Register;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Data {

    private HashMap<Integer, User> users;
    private HashMap<String, Integer> userIDs;
    private LinkedList<String> filteredWords;

    private static class DataSingletoneHolder{
        private static Data instance = new Data();
    }

    private Data(){
        userIDs=new HashMap<>();
        users=new HashMap<>();
        filteredWords= new LinkedList<>();
        filteredWords.add("Idan");
        filteredWords.add("Mor");
    }

    public static Data getData(){
        return DataSingletoneHolder.instance;
    }

    public Boolean isRegistered(int index){
        return users.containsKey(index);
    }
    public Boolean isRegistered(String username){ return isRegistered(userIDs.get(username));}

    public void register(Register message, int connId) {
        User u = new User(message.userName, message.password, message.birthday);
        users.put(connId,u);
        userIDs.put(u.getUserName(), connId);
    }

    public boolean isUserAndPassCorrect(int index, String userName, String password) {
        return users.get(index).isValidUserAndPass(userName, password);
    }
    public boolean isLoggedIn(int index) {
        return users.get(index).isLoggedIn();
    }
    public void login(int index) {
        users.get(index).LogIn();
    }
    public void logout(int index) {
        users.get(index).logOut();
    }
    public boolean isFollowing(int connectionID, String userName) {
        return users.get(connectionID).isFollowing(userName);
    }
    public Integer getIdByUsername(String username) {
        return userIDs.get(username);
    }

    public String getUsernameById(int id){
        return users.get(id).getUserName();
    }

    public LinkedList<Integer> getFollowersId(int connectionID) {
        LinkedList<String> usernames = users.get(connectionID).getFollowers();
        LinkedList<Integer> userIds = new LinkedList<>();
        for (String un: usernames){
            userIds.add(userIDs.get(un));
        }
        return userIds;
    }

    public String filter(String content) {
        String ans=content;
        for (String s: filteredWords){
            int indexOfBadWord = ans.indexOf(s);
            while (indexOfBadWord != -1) {
                ans = ans.substring(0, indexOfBadWord);
                ans = ans+"<filtered>"+ans.substring(indexOfBadWord+s.length());
                indexOfBadWord = ans.indexOf(s);
            }
        }
        return ans;
    }

    public LinkedList<String> getLoggedInUserStat(int connectionID) {
        LinkedList<String> ans = new LinkedList<>();
        String myUsername = users.get(connectionID).getUserName();
        for(User u: users.values())
            if (!u.getUserName().equals(myUsername) & !u.isBlocking(myUsername))
            ans.add(u.getStat());
        return ans;
    }

    public String getStat(String username) {
        int index= userIDs.get(username);
        return users.get(index).getStat();
    }


    public void Block(int connectionID, String userName) {
        users.get(connectionID).Block(userName);
        users.get(userIDs.get(userName)).unFollow(users.get(connectionID).getUserName());
    }

    public boolean isBlockedby(int connectionId, String username){
        return users.get(userIDs.get(username)).isBlocking(users.get(connectionId).getUserName());
    }


    public void unfollow(int connectionID, String userName) {
        users.get(connectionID).unFollow(userName);
        users.get(userIDs.get(userName)).removeFollower(users.get(connectionID).getUserName());
    }


    public void follow(int connectionID, String userName) {
        users.get(connectionID).Follow(userName);
        users.get(userIDs.get(userName)).addFollower(users.get(connectionID).getUserName());
    }


    public void post(int connectionID, String content) {
        users.get(connectionID).addPost(content);
    }


    public void PM(int connectionID, String content) {
        users.get(connectionID).addPM(content);
    }


    public void addToUnotifiedList(int connId, Message msg) {
        users.get(connId).addToUnnotifiedList(msg);
    }


    public LinkedList<Message> getUnnotifiedMessages(int connectionID) {
        return users.get(connectionID).getUnnotifiedMessages();
    }




}
