package com.ja.wantrip.app.post.controller;

import com.ja.wantrip.app.base.exception.ActorCanNotRemoveException;
import com.ja.wantrip.app.base.rq.Rq;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.service.MemberService;
import com.ja.wantrip.app.post.entity.Answer;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.post.form.AnswerForm;
import com.ja.wantrip.app.post.service.AnswerService;
import com.ja.wantrip.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final PostService postService;
    private final AnswerService answerService;
    private final MemberService memberService;

    @PostMapping("/create/{id}")
    public String createAnswer(Principal principal, Model model, @Valid AnswerForm answerForm, @PathVariable long id, BindingResult bindingResult) {
        Post post = this.postService.getPost(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post/detail";
        }

        Member member = memberService.getUser(principal.getName());


        Answer answer = answerService.create(post, member, answerForm.getContent());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글의 답변을 제출하였습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyAnswer(AnswerForm answerForm, @PathVariable("id") Long id, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerForm.setContent(answer.getContent());

        return "post/answerModify";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyAnswer(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerService.modify(answer, answerForm.getContent());

        return "redirect:/post/%d".formatted(answer.getPost().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteAnswer(Principal principal, @PathVariable("id") Long id) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        answerService.delete(answer);

        return "redirect:/post/%d".formatted(answer.getPost().getId());
    }

}
