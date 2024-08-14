package com.example.social_media_app.repository;

import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    long countByPost(Post post);

    List<Like> findByPost(Post post);

    void deleteByPost(Post post);

    void deleteByUser(User user);
}
