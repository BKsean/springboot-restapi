package com.example.springbootrestapi.user;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private UserDaoService userService;

    public AdminUserController(UserDaoService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){

        List<User> users = userService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
        return mapping;
    }

//    @GetMapping("/v1/users/{id}")
//@GetMapping(value = "/users/{id}",params = "version=1")
//    @GetMapping(value = "/users/{id}",headers = "X-API-VERSION=1")
    @GetMapping(value = "/v1/users/{id}",produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id){

        User user = userService.findOne(id);
        if(user == null){
            throw new UserNotFoundExceptrion(String.format("ID[%s] not found",id));
        }


        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

//    @GetMapping("/v2/users/{id}")
//@GetMapping(value = "/users/{id}",params = "version=2")
//@GetMapping(value = "/users/{id}",headers = "X-API-VERSION=2")
@GetMapping(value = "/v1/users/{id}",produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id){

        User user = userService.findOne(id);
        if(user == null){
            throw new UserNotFoundExceptrion(String.format("ID[%s] not found",id));
        }

        // User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user,userV2);
        userV2.setGrade("VIP");


        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
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

