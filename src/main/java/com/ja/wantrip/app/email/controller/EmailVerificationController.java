package com.ja.wantrip.app.email.controller;

import com.ja.wantrip.app.base.dto.RsData;
import com.ja.wantrip.app.base.rq.Rq;
import com.ja.wantrip.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/emailVerification")
public class EmailVerificationController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/verify")
    public String verify(long memberId, String code) {
        RsData verifyEmailRsData = memberService.verifyEmail(memberId, code);

        if (verifyEmailRsData.isFail()) {
            return Rq.redirectWithMsg("/", verifyEmailRsData);
        }

        String successMsg = verifyEmailRsData.getMsg();

        if (rq.isLogined()) {
            return Rq.redirectWithMsg("/member/login", successMsg);
        }

        return Rq.redirectWithMsg("/", successMsg);
    }
}

