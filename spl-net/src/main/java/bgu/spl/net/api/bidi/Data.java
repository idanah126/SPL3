package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.ReadWriteList;
import bgu.spl.net.impl.ReadWriteMap;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;
import bgu.spl.net.srv.messages.Register;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Data {

    private ReadWriteMap<Integer, User> users;
    private ReadWriteMap<String, Integer> userIDs;
    private ReadWriteList<String> filteredWords;

    private static class DataSingletoneHolder{
        private static Data instance = new Data();
    }

    private Data(){
        userIDs=new ReadWriteMap<>( new HashMap<>() );
        users=new ReadWriteMap<>( new HashMap<>() );
        filteredWords= new ReadWriteList<>();
        filteredWords.add("Idan");
        filteredWords.add("Mor");
    }

    public static Data getData(){
        return DataSingletoneHolder.instance;
    }

    public Boolean isRegistered(int index){
        return users.containsKey(index);
    }
    public Boolean isRegistered(String username){ return userIDs.containsKey(username);}

    public void register(Register message, int connId) {
        User u = new User(message.userName, message.password, message.birthday);
        users.put(connId,u);
        userIDs.put(u.getUserName(), connId);
    }

    public boolean isUserAndPassCorrect(int index, String userName, String password) {
        synchronized (users.get(index))
        {  return users.get(index).isValidUserAndPass(userName, password); }
    }
    public boolean isLoggedIn(int index) {
        if (!users.containsKey(index)) return false;
        synchronized (users.get(index))
        { return users.get(index).isLoggedIn(); }
    }
    public void login(int index) {
        synchronized (users.get(index))
        { users.get(index).LogIn(); }
    }
    public void logout(int index) {
        synchronized (users.get(index))
        { users.get(index).logOut(); }
    }
    public boolean isFollowing(int connectionID, String userName) {
        synchronized (users.get(connectionID))
        { return users.get(connectionID).isFollowing(userName); }
    }
    public Integer getIdByUsername(String username) {
        return userIDs.get(username);
    }

    public String getUsernameById(int id){
        synchronized (users.get(id))
        { return users.get(id).getUserName(); }
    }

    public LinkedList<Integer> getFollowersId(int connectionID) {
        LinkedList<String> usernames;
        synchronized (users.get(connectionID))
        { usernames = users.get(connectionID).getFollowers(); }
        LinkedList<Integer> userIds = new LinkedList<>();
        for (String un: usernames){
            userIds.add(userIDs.get(un));
        }
        return userIds;
    }

    public String filter(String content) {
        String ans=content;
        for (int i=0; i<filteredWords.size(); i++){
            int indexOfBadWord = ans.indexOf(filteredWords.get(i));
            while (indexOfBadWord != -1) {
                ans = ans.substring(0, indexOfBadWord)+"<filtered>"+ans.substring(indexOfBadWord+filteredWords.get(i).length());
                indexOfBadWord = ans.indexOf(filteredWords.get(i));
            }
        }
        return ans;
    }

    public LinkedList<String> getLoggedInUserStat(int connectionID) {
        LinkedList<String> ans = new LinkedList<>();
        String myUsername;
        synchronized (users.get(connectionID)){ myUsername = users.get(connectionID).getUserName();}
        for(Map.Entry<Integer, User> entry: users.getSet())
            synchronized (entry.getValue()) {
                if (!entry.getValue().getUserName().equals(myUsername) & !entry.getValue().isBlocking(myUsername) & entry.getValue().isLoggedIn() & !users.get(connectionID).isBlocking(entry.getValue().getUserName()))
                    ans.add(entry.getValue().getStat());
            }
        return ans;
    }

    public String getStat(String username) {
        int index= userIDs.get(username);
        synchronized (users.get(index)){
        return users.get(index).getStat(); }
    }


    public void Block(int connectionID, String userName) {
        String myUsername;
        synchronized (users.get(connectionID)) {
            users.get(connectionID).Block(userName);
            myUsername= users.get(connectionID).getUserName();
        }
        synchronized (users.get(userIDs.get(userName))) {
            users.get(userIDs.get(userName)).unFollow(myUsername);
            users.get(userIDs.get(userName)).removeFollower(myUsername);
        }

    }

    public boolean isBlockedby(int connectionId, String username){
        String myUsername;
        synchronized (users.get(connectionId)) {
            myUsername= users.get(connectionId).getUserName();
        }
        synchronized (users.get(userIDs.get(username))) {
        return users.get(userIDs.get(username)).isBlocking(myUsername); }
    }

    public boolean isBlockedby(String userName, int connectionID){
        synchronized (users.get(connectionID)){
            return users.get(connectionID).isBlocking(userName);    }
    }


    public void unfollow(int connectionID, String userName) {
        String myUsername;
        synchronized (users.get(connectionID)) {
            users.get(connectionID).unFollow(userName);
            myUsername= users.get(connectionID).getUserName();
        }
        synchronized (users.get(userIDs.get(userName))) {
        users.get(userIDs.get(userName)).removeFollower(myUsername); }
    }


    public void follow(int connectionID, String userName) {
        String myUsername;
        synchronized (users.get(connectionID)) {
            users.get(connectionID).Follow(userName);
            myUsername= users.get(connectionID).getUserName();
        }
        synchronized (users.get(userIDs.get(userName))) {
            users.get(userIDs.get(userName)).addFollower(myUsername);
        }
    }


    public void post(int connectionID, String content) {
        synchronized (users.get(connectionID)) {
            users.get(connectionID).addPost(content);
        }
    }


    public void PM(int connectionID, String content) {
        synchronized (users.get(connectionID)) {
        users.get(connectionID).addPM(content);}
    }


    public void addToUnotifiedList(int connId, Message msg) {
        synchronized (users.get(connId)) {
        users.get(connId).addToUnnotifiedList(msg);}
    }


    public LinkedList<Message> getUnnotifiedMessages(int connectionID) {
        synchronized (users.get(connectionID)) {
            return users.get(connectionID).getUnnotifiedMessages(); }
    }




}
