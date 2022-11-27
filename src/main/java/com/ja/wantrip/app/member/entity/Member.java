package com.ja.wantrip.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ja.wantrip.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private boolean emailVerified;

    private long restCash;

    private String nickname;

    public Member(long id) {
        super(id);
    }
}
