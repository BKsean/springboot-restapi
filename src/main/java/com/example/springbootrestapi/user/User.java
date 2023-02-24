package com.example.springbootrestapi.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*@JsonIgnoreProperties(value = {"password","ssn"})*/
//@JsonFilter("UserInfo")//이름은 임의지정
public class User {

    private Integer id;
    @Size(min=2, message = "Name은 2글자이상 입력해 주세요")
    private String name;
    @Past
    private Date joinDate;

    private String password;
    private String ssn;
}
