package com.example.social_media_app.repository;

import com.example.social_media_app.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    void deleteByPostId(Long postId);

    void deleteByUserId(Long userId);
}
