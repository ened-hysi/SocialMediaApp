package com.example.social_media_app.repository;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(Optional<User> user);

    void deleteByUserId(Long userId);

    //fetch posts created by a list of users that the current user is following.
    @Query("SELECT p FROM Post p WHERE p.user IN :following ORDER BY p.createdAt DESC")
    Page<Post> findByUserInOrderByCreatedAtDesc(@Param("following") List<User> following, Pageable pageable);


    // fetch posts that are created by users who are not in the current user's following list and are not the current user themselves.
    @Query("SELECT p FROM Post p WHERE p.user NOT IN :following AND p.user != :currentUser ORDER BY p.createdAt DESC")
    Page<Post> findByUserNotInOrderByCreatedAtDesc(@Param("following") List<User> following, @Param("currentUser") User currentUser, Pageable pageable);
}
