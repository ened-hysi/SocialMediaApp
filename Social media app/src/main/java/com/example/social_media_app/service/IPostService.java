package com.example.social_media_app.service;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;

import java.util.List;
import java.util.Optional;

public interface IPostService {

    Post createPost(Post post, User user);

    Optional<Post> findById(Long id);

    Post updatePost(Long id,Post updatedPost);

    void deletePost(Long id);

    List<Post> getAllPosts();

    List<Post> findPostsByUser(Long userId);

}
