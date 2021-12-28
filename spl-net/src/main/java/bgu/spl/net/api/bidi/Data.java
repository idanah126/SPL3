package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Register;

import java.util.HashMap;
import java.util.LinkedList;

public class Data {

    private HashMap<Integer, User> users;
    private HashMap<String, Integer> userIDs;


    private static class DataSingletoneHolder{
        private static Data instance = new Data();
    }

    private Data(){
        userIDs=new HashMap<>();
        users=new HashMap<>();
    }

    public static Data getData(){
        return DataSingletoneHolder.instance;
    }

    public Boolean isRegistered(int index){
        return users.containsKey(index);
    }

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





}
