package com.ja.wantrip.app.post.service;

import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.post.entity.Answer;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.post.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Post post, Member author, String content) {
        Answer answer = Answer.builder()
                .content(content)
                .author(author)
                .build();
        post.addAnswer(answer);

        answerRepository.save(answer);

        return answer;
    }
}
