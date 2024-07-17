package com.example.social_media_app.controller;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findPostById (@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id).get());
    }

    @GetMapping
    public ResponseEntity<List<Post>> findAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Post>> findPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.findPostsByUser(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id ,@RequestBody Post post) {
        return ResponseEntity.ok(postService.updatePost(id,post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
