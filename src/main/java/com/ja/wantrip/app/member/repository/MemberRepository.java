package com.ja.wantrip.app.member.repository;

import com.ja.wantrip.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
