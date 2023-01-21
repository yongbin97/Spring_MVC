package com.example.spring_mvc_1_servlet.domain.member;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler;

@Getter @Setter
public class Member {

    private Long id;
    private String username;
    private int age;

    public Member() {
    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;

    }
}
