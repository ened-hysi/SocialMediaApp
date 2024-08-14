package com.example.social_media_app.service;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.FollowRepository;
import com.example.social_media_app.repository.PostRepository;
import com.example.social_media_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowService followService;


    ModelMapper mapper = new ModelMapper();

    public PostService(PostRepository postRepository, UserRepository userRepository, FollowService followService) {
        this.postRepository = postRepository;

        this.userRepository = userRepository;
        this.followService = followService;
    }

    @Override
    public Post createPost(Post post , User user) {
        post.setUser(user);
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post updatePost(Long id ,Post updatedPost) {
//        Post post = postRepository.findById(id).get();
////        post.setContent(updatedpost.getContent());
//        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
//        mapper.map(updatedPost , post);
//        return postRepository.save(post);

        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setContent(updatedPost.getContent());
                    return postRepository.save(existingPost);
                })
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public void deletePost(Long id) {
       postRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findPostsByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return postRepository.findByUser(user);
    }

    @Transactional
    public void deletePostsByUser(Long userId) {
        postRepository.deleteByUserId(userId);
    }

    public Page<Post> getFeed(Long userId, Pageable pageable) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> following = followService.getFollowing(userId);

        if (following.isEmpty()) {
            // If not following anyone, return posts from all users except the current user
            return postRepository.findByUserNotInOrderByCreatedAtDesc(Collections.emptyList(), currentUser, pageable);
        }

        // First, get posts from users the current user is following
        Page<Post> followedPosts = postRepository.findByUserInOrderByCreatedAtDesc(following, pageable);

        // If there aren't enough posts from followed users, get posts from other users
        if (followedPosts.getContent().size() < pageable.getPageSize()) {
            int remainingPosts = pageable.getPageSize() - followedPosts.getContent().size();
            Page<Post> otherPosts = postRepository.findByUserNotInOrderByCreatedAtDesc(
                    following, currentUser, Pageable.ofSize(remainingPosts));

            // Combine the two lists
            List<Post> allPosts = new ArrayList<>(followedPosts.getContent());
            allPosts.addAll(otherPosts.getContent());

            // Create a new Page object with the combined results
            return new PageImpl<>(allPosts, pageable, followedPosts.getTotalElements() + otherPosts.getTotalElements());
        }

        return followedPosts;

    }

}
