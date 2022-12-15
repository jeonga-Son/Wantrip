package com.ja.wantrip.app.post.entity;

import com.ja.wantrip.app.base.entity.BaseEntity;
import com.ja.wantrip.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Answer extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    private Member author;

    @ManyToOne
    private Post post;
}
