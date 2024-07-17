package com.example.social_media_app.controller;

import com.example.social_media_app.Dto.CommentDto;
import com.example.social_media_app.models.Comment;
import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.service.CommentService;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;

    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDto, @RequestParam Long postId, @RequestParam Long userId) {
        Optional<Post> post = postService.findById(postId);
        Optional<User> user = userService.findById(userId);

        Comment comment = commentService.createComment(commentDto ,user.get(), post.get());
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findCommentById (@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findCommentById(id).get());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id ,@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(id,comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
