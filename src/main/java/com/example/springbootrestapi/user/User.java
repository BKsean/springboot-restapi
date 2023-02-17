package com.example.springbootrestapi.user;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    @Size(min=2, message = "Name은 2글자이상 입력해 주세요")
    private String name;
    @Past
    private Date joinDate;
}
