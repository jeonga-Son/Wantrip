package com.ja.wantrip.app.post.service;

import com.ja.wantrip.app.base.exception.DataNotFoundException;
import com.ja.wantrip.app.member.entity.Member;
import com.ja.wantrip.app.post.entity.Post;
import com.ja.wantrip.app.post.repository.PostRepository;
import com.ja.wantrip.app.postTag.entity.PostTag;
import com.ja.wantrip.app.postTag.service.PostTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostTagService postTagService;

    @Transactional
    public Post write(Member author, String subject, String content, String contentHtml, String postTagContents) {
        Post post = Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .author(author)
                .build();
        postRepository.save(post);

        applyPostTags(post, postTagContents);

        return post;
    }

    @Transactional
    public void applyPostTags(Post post, String postTagContents) {
        postTagService.applyPostTags(post, postTagContents);
    }

    public Optional<Post> findForPrintById(long id) {
        Optional<Post> opPost = findById(id);

        if (opPost.isEmpty()) return opPost;

        List<PostTag> postTags = getPostTags(opPost.get());

        opPost.get().getExtra().put("postTags", postTags);

        return opPost;
    }

    public List<PostTag> getPostTags(Post post) {
        return postTagService.getPostTags(post);
    }

    public Optional<Post> findById(long postId) {
        return postRepository.findById(postId);
    }

    public boolean actorCanModify(Member author, Post post) {
        return author.getId().equals(post.getAuthor().getId());
    }

    @Transactional
    public void modify(Post post, String subject, String content, String contentHtml, String postTagContents) {
        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(contentHtml);
        applyPostTags(post, postTagContents);
    }

    public boolean actorCanRemove(Member author, Post post) {
        return actorCanModify(author, post);
    }

    @Transactional
    public void remove(Post post) {
        postRepository.delete(post);
    }

//    public List<Post> findAllForPrintByPostIdOrderByIdDesc(long postId) {
//        List<Post> posts = postRepository.findAllByPostIdOrderByIdDesc(postId);
//        loadForPrintData(posts);
//
//        return posts;
//    }

    public void loadForPrintData(List<Post> posts) {
        long[] ids = posts
                .stream()
                .mapToLong(Post::getId)
                .toArray();

        List<PostTag> postTagsByPostIds = postTagService.getPostTagsByPostIdIn(ids);

        Map<Long, List<PostTag>> postTagsByPostIdsMap = postTagsByPostIds.stream()
                .collect(groupingBy(
                        postTag -> postTag.getPost().getId(), toList()
                ));

        posts.stream().forEach(post -> {
            List<PostTag> postTags = postTagsByPostIdsMap.get(post.getId());

            if (postTags == null || postTags.size() == 0) return;

            post.getExtra().put("postTags", postTags);
        });
    }

    public List<Post> findAll() {
        List<Post> posts = postRepository.findAll();

        return posts;
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
        return this.postRepository.findAll(pageable);
    }

    public Post getPost(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("%d번 글을 찾을 수 없습니다.".formatted(id)));
    }

    @Transactional
    public void vote(Post post, Member member) {
        post.getVoter().add(member);

        postRepository.save(post);
    }

    public List<PostTag> getPostTags(Member author, String postKeywordContent) {
        List<PostTag> postTags = postTagService.getPostTags(author, postKeywordContent);

        loadForPrintDataOnPostTagList(postTags);

        return postTags;
    }

    private void loadForPrintDataOnPostTagList(List<PostTag> postTags) {
        List<Post> posts = postTags
                .stream()
                .map(PostTag::getPost)
                .collect(toList());

        loadForPrintData(posts);
    }
}
