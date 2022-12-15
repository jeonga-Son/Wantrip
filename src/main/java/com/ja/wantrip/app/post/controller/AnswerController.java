package com.ja.wantrip.app.post.controller;

import com.ja.wantrip.app.base.rq.Rq;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.service.MemberService;
import com.ja.wantrip.app.post.entity.Answer;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.post.form.AnswerForm;
import com.ja.wantrip.app.post.service.AnswerService;
import com.ja.wantrip.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/create/{id}")
//    public String createAnswer(Model model, @Valid AnswerForm answerForm, @PathVariable long id) {
//        Member author = rq.getMember();
//        Post post = postService.findById(id).get();
//        Answer answer = answerService.write(author, answerForm.getContent());
//        model.addAttribute("answer", answer);
//
//        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글의 답변을 제출하였습니다.".formatted(post.getId()));
//    }
}
