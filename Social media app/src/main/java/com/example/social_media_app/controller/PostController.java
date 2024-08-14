package com.example.social_media_app.controller;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.security.AuthenticationUtils;
import com.example.social_media_app.service.CommentService;
import com.example.social_media_app.service.LikeService;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;
    private final CommentService commentService;



    public PostController(PostService postService, UserService userService, LikeService likeService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {

        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        User currentUser = userService.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post createdPost = postService.createPost(post, currentUser);
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,@RequestBody Post updatedPost){
//        Long currentUserId = AuthenticationUtils.getCurrentUserId();
//        Optional<Post> existingPost = postService.findById(id);
//
//        if (existingPost.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Post post = existingPost.get();
//        if (!post.getUser().getId().equals(currentUserId)) {
//            return ResponseEntity.status(403).build();
//        }
//
//        Post updated = postService.updatePost(id, updatedPost);
//        return ResponseEntity.ok(updated);

        Optional<Post> existingPost = postService.findById(id);

        if (existingPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = existingPost.get();
        boolean isOwner = AuthenticationUtils.isCurrentUser(post.getUser().getId());
        boolean isAdmin = AuthenticationUtils.isCurrentUserAdmin();

        log.info("Is Owner: " + isOwner);
        log.info("Is Admin: " + isAdmin);

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Post updated = postService.updatePost(id, updatedPost);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        Long currentUserId = AuthenticationUtils.getCurrentUserId();
//        Optional<Post> existingPost = postService.findById(id);
//
//        if (existingPost.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Post post = existingPost.get();
//        if (!post.getUser().getId().equals(currentUserId)) {
//            return ResponseEntity.status(403).build();
//        }
//
//        postService.deletePost(id);
//        return ResponseEntity.noContent().build();

        Optional<Post> existingPost = postService.findById(id);

        if (existingPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = existingPost.get();
        boolean isOwner = AuthenticationUtils.isCurrentUser(post.getUser().getId());
        boolean isAdmin = AuthenticationUtils.isCurrentUserAdmin();

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentService.deleteCommentsByPost(id);
        likeService.deletePostLikes(id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> findAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> findPostById(@PathVariable Long id) {
        Optional<Post> post = postService.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<Post>> findPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.findPostsByUser(userId));
    }

    @GetMapping("/posts/my-posts")
    public ResponseEntity<List<Post>> findCurrentUserPosts() {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(postService.findPostsByUser(currentUserId));
    }

    @GetMapping("/")
    public ResponseEntity<Page<Post>> getFeed(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Get the current user's ID
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());

        // Create a Pageable object with the provided parameters
        Pageable pageable = PageRequest.of(page, size);

        // Get the feed for the current user
        Page<Post> feed = postService.getFeed(userId, pageable);

        return ResponseEntity.ok(feed);
    }
}
