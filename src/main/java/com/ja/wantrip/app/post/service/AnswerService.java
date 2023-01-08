package com.ja.wantrip.app.post.service;

import com.ja.wantrip.app.base.exception.DataNotFoundException;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.post.entity.Answer;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.post.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Answer getAnswer(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("답변을 찾을 수 없습니다."));
    }

    public Optional<Answer> findById(long answerId) {
        return answerRepository.findById(answerId);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    @Transactional
    public void modify(Answer answer, String content) {
        answer.setContent(content);
    }
}
