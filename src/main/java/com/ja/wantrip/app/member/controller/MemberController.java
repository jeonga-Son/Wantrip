package com.ja.wantrip.app.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/join")
    public String showJoin() {
        return "join";
    }
}
