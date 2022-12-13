package com.ja.wantrip.app.post.repository;

import com.ja.wantrip.app.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
//    List<Post> findAllByPostIdOrderByIdDesc(long postId);
}
