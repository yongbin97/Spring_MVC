package com.example.spring_mvc_1_servlet.web.frontcontroller.v2.controller;

import com.example.spring_mvc_1_servlet.domain.member.Member;
import com.example.spring_mvc_1_servlet.domain.member.MemberRepository;
import com.example.spring_mvc_1_servlet.web.frontcontroller.MyView;
import com.example.spring_mvc_1_servlet.web.frontcontroller.v2.ControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MemberListControllerV2 implements ControllerV2 {
    MemberRepository memberRepository = MemberRepository.getInstance();


    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);
        return new MyView("/WEB-INF/views/members.jsp");
    }
}
