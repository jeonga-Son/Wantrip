package com.ja.wantrip.app.member.controller;

import com.ja.wantrip.app.base.rq.Rq;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.form.JoinForm;
import com.ja.wantrip.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/member/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }

        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname());

        return Rq.redirectWithMsg("/member/login", "회원가입이 완료되었습니다. " + joinForm.getEmail() + "로 이메일인증코드를 발송하였습니다. 인증 후 로그인해주세요");
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findId")
    public String showFindId() {
        return "member/findId";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findId")
    public String findId(String email, Model model) {
        Member member = memberService.findByEmail(email).orElse(null);

        if (member == null) {
            return rq.historyBack("일치하는 회원이 존재하지 않습니다.");
        }

        return Rq.redirectWithMsg("/member/login?username=%s".formatted(member.getUsername()), "해당 이메일로 가입한 계정의 아이디는 '%s' 입니다.".formatted(member.getUsername()));
    }


    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(String username, String email, Model model) {
        return "member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile() {
        return "member/profile";
    }

}
