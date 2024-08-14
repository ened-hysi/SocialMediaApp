//package com.example.social_media_app.controller;
//
//
//import com.example.social_media_app.Dto.CommentDTO;
//import com.example.social_media_app.models.Post;
//import com.example.social_media_app.models.User;
//import com.example.social_media_app.security.AuthenticationUtils;
//import com.example.social_media_app.service.CommentService;
//import com.example.social_media_app.service.PostService;
//import com.example.social_media_app.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final UserService userService;
//    private final PostService postService;
//    private final CommentService commentService;
//
//    public AdminController(UserService userService, PostService postService, CommentService commentService) {
//        this.userService = userService;
//        this.postService = postService;
//        this.commentService = commentService;
//    }
//
//    @PatchMapping("/updateUser/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id , @RequestBody User updatedUser) {
//        try {
//            User user = userService.updateUser(id, updatedUser);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//
//    }
//
//    @DeleteMapping("/deleteUser/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @DeleteMapping("/deletePost/{id}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        Optional<Post> existingPost = postService.findById(id);
//
//        if (existingPost.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Post post = existingPost.get();
//        postService.deletePost(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//}
