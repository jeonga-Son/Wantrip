package com.ja.wantrip.app.member.service;

import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(String username, String password, String email, String nickname) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return member;
    }
}
