package com.ja.wantrip.app.email.repository;

import com.ja.wantrip.app.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}

