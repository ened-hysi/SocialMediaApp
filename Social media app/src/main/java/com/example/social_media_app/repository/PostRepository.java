package com.example.social_media_app.repository;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(Optional<User> user);
}
