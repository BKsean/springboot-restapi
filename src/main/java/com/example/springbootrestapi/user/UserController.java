package com.example.springbootrestapi.user;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public EntityModel<User> retrieveUser(@PathVariable int id){

        User user = userService.findOne(id);
        if(user == null){
            throw new UserNotFoundExceptrion(String.format("ID[%s] not found",id));
        }

        //HATEOAS
        EntityModel<User> model = EntityModel.of(user);
        /*WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.
                linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());*/
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        WebMvcLinkBuilder linkTo2 = linkTo(methodOn(this.getClass()).retrieveUser(id));
        model.add(linkTo.withRel("all-Users"));
        model.add(linkTo2.withRel("search fisrt user"));
        return model;
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
