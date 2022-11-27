package com.ja.wantrip.app.member.service;

import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(String username, String password, String email, String nickname) {

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
