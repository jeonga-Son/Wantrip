package com.ja.wantrip.app.postTag.entity;

import com.ja.wantrip.app.base.entity.BaseEntity;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.postkeyword.entity.PostKeyword;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class PostTag extends BaseEntity {
    @ManyToOne
    @ToString.Exclude
    @OnDelete(action = CASCADE)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Member member;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private PostKeyword postKeyword;
}
