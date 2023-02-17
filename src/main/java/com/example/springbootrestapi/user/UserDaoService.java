package com.example.springbootrestapi.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {

    private static List<User> userList = new ArrayList<User>();

    private static  int userCount = 3;
    static{
        userList.add(new User(1,"seungkwan",new Date()));
        userList.add(new User(2,"hm",new Date()));
        userList.add(new User(3,"bk",new Date()));
    }

    public List<User> findAll(){
        return userList;
    }

    public User save(User user){
        if(user.getId() == null){
            user.setId(++userCount);
        }
        userList.add(user);
        return user;
    }
    public User findOne(int id){
        for(User user : userList){
            if(user.getId() == id){
                return user;
            }
        }

        return null;
    }

    public User deleteUser(int id){
        Iterator<User> iterator = userList.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }

    public User updateUser(User user){

        for(int i=0; i < userList.size(); i++){
            if(userList.get(i).getId() == user.getId()){
                userList.get(i).setName(user.getName());
                userList.get(i).setJoinDate(user.getJoinDate());
                return user;
            }
        }
        return null;

    }
}
