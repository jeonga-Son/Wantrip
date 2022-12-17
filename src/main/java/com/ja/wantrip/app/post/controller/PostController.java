package com.ja.wantrip.app.post.controller;

import com.ja.wantrip.app.base.exception.ActorCanNotModifyException;
import com.ja.wantrip.app.base.exception.ActorCanNotRemoveException;
import com.ja.wantrip.app.base.rq.Rq;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.member.service.MemberService;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.post.form.AnswerForm;
import com.ja.wantrip.app.post.form.PostForm;
import com.ja.wantrip.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final Rq rq;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid PostForm postForm) {
        Member author = rq.getMember();
        Post post = postService.write(author, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getPostTagContents());

        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 생성되었습니다.".formatted(post.getId()));
    }

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = this.postService.getList(page);

        model.addAttribute("paging", paging);

        return "post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model, AnswerForm answerForm) {
        Post post = postService.findForPrintById(id).get();

        Member actor = rq.getMember();

        model.addAttribute("post", post);

        return "post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String showModify(@PathVariable long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid PostForm postForm, @PathVariable long id) {
        Post post = postService.findById(id).get();
        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        postService.modify(post, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getPostTagContents());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 수정되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove/{id}")
    public String remove(@PathVariable long id) {
        Post post = postService.findById(id).get();
        Member actor = post.getAuthor();

        if (postService.actorCanRemove(actor, post) == false) {
            throw new ActorCanNotRemoveException();
        }

        postService.remove(post);

        return Rq.redirectWithMsg("/post/list", "%d번 글이 삭제되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String showVote(Principal principal, @PathVariable("id") Long id) {
        Post post = postService.getPost(id);
        Member member = memberService.getUser(principal.getName());

        postService.vote(post, member);
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글에 좋아요를 눌렀습니다.".formatted(post.getId()));
    }

}
