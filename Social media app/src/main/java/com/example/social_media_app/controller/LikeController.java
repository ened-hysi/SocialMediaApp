package com.example.social_media_app.controller;

import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.LikeRepository;
import com.example.social_media_app.service.LikeService;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    private final UserService userService;

    private final PostService postService;

    public LikeController(LikeService likeService, UserService userService, PostService postService) {
        this.likeService = likeService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Like> addLike(@RequestParam Long postId, @RequestParam Long userId) {
        Optional<Post> post = postService.findById(postId);
        Optional<User> user = userService.findById(userId);

        Like like = likeService.addLike(user.get(), post.get());
        return ResponseEntity.ok(like);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeService.removeLike(id);
        return ResponseEntity.noContent().build();
    }

}
