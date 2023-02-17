package com.example.springbootrestapi.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private UserDaoService userService;

    public UserController(UserDaoService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){

        User user = userService.findOne(id);
        if(user == null){
            throw new UserNotFoundExceptrion(String.format("ID[%s] not found",id));
        }
        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userService.save(user);
ResponseEntity<String> test = new ResponseEntity<>("dd", HttpStatus.OK);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(1)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = userService.deleteUser(id);

        if(user == null){
            throw new UserNotFoundExceptrion(String.format("ID[%s] not found",id));
        }
    }

    @PutMapping("/users")
    public void updateUser(@RequestBody User user){
        User updatedUser = userService.updateUser(user);

        if(updatedUser == null){
            throw new UserNotFoundExceptrion(String.format("ID[%s] not found",user));
        }
    }

}
