package com.ja.wantrip.app.member.service;

import com.ja.wantrip.app.AppConfig;
import com.ja.wantrip.app.base.dto.RsData;
import com.ja.wantrip.app.base.exception.DataNotFoundException;
import com.ja.wantrip.app.email.service.EmailService;
import com.ja.wantrip.app.email.service.EmailVerificationService;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.entity.emum.AuthLevel;
import com.ja.wantrip.app.member.exception.AlreadyJoinException;
import com.ja.wantrip.app.member.repository.MemberRepository;
import com.ja.wantrip.app.security.dto.MemberContext;
import com.ja.wantrip.app.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;

    @Transactional
    public Member join(String username, String password, String email, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(AuthLevel.NORMAL)
                .build();

        memberRepository.save(member);

        emailVerificationService.send(member);

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public RsData verifyEmail(long id, String verificationCode) {
        RsData verifyVerificationCodeRs = emailVerificationService.verifyVerificationCode(id, verificationCode);

        if (verifyVerificationCodeRs.isSuccess() == false) {
            return verifyVerificationCodeRs;
        }

        Member member = memberRepository.findById(id).get();
        member.setEmailVerified(true);

        return RsData.of("S-1", "?????????????????? ?????????????????????.");
    }

    public Optional<Member> findByUsernameAndEmail(String username, String email) {
        return memberRepository.findByUsernameAndEmail(username, email);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member getUser(String username) {
        return this.memberRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("???????????? ????????????."));
    }

    @Transactional
    public RsData sendTempPasswordToEmail(Member actor) {
        String title = "[" + AppConfig.getSiteName() + "] ?????? ???????????? ??????";
        String tempPassword = Util.getTempPassword(6);
        String body = "<h1>?????? ???????????? : " + tempPassword + "</h1>";
        body += "<a href=\"" + AppConfig.getSiteBaseUrl() + "/member/login\" target=\"_blank\">????????? ????????????</a>";

        RsData sendResultData = emailService.sendEmail(actor.getEmail(), title, body);

        if (sendResultData.isFail()) {
            return sendResultData;
        }

        setTempPassword(actor, tempPassword);

        return RsData.of("S-1", "????????? ?????????????????? ?????? ??????????????? ?????????????????????.");
    }

    @Transactional
    private void setTempPassword(Member actor, String tempPassword) {
        actor.setPassword(passwordEncoder.encode(tempPassword));
    }

    @Transactional
    public RsData modifyPassword(Member member, String password, String oldPassword) {
        Optional<Member> opMember = memberRepository.findById(member.getId());

        if (passwordEncoder.matches(oldPassword, opMember.get().getPassword()) == false) {
            return RsData.of("F-1", "?????? ??????????????? ???????????? ????????????.");
        }

        opMember.get().setPassword(passwordEncoder.encode(password));

        return RsData.of("S-1", "??????????????? ?????????????????????.");
    }

    @Transactional
    public RsData beAuthor(Member member, String nickname) {
        Optional<Member> opMember = memberRepository.findByNickname(nickname);

        if (opMember.isPresent()) {
            return RsData.of("F-1", "?????? ????????? ?????? ??????????????????.");
        }

        opMember = memberRepository.findById(member.getId());

        opMember.get().setNickname(nickname);
        forceAuthentication(opMember.get());

        return RsData.of("S-1", "?????? ???????????? ????????? ???????????????.");
    }

    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.getAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        member.getPassword(),
                        memberContext.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
