package com.example.springbootrestapi.user;

import com.fasterxml.jackson.annotation.JsonFilter;
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
@JsonFilter("UserInfoV2")//이름은 임의지정
public class UserV2 extends User{

    private String grade;

}
