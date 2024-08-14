package com.example.social_media_app.controller;

import com.example.social_media_app.Dto.UserSearchResultDTO;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.security.AuthenticationUtils;
import com.example.social_media_app.security.CustomUserDetails;
import com.example.social_media_app.service.CommentService;
import com.example.social_media_app.service.LikeService;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    public UserController(UserService userService, PostService postService, CommentService commentService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id).get());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id , @RequestBody User updatedUser) {

        if (!AuthenticationUtils.isCurrentUser(id) && !AuthenticationUtils.isCurrentUserAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        if (!AuthenticationUtils.isCurrentUser(id) && !AuthenticationUtils.isCurrentUserAdmin()) {
            return ResponseEntity.status(403).build();
        }
        likeService.deleteLikesByUser(id);
        commentService.deleteCommentsByUser(id);
        postService.deletePostsByUser(id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUsers(@PathVariable String username) {
        List<UserSearchResultDTO> users = userService.searchUsers(username);

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "No users found",
                    "searchTerm", username
            ));
        }
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        Map<String, Object> userProfile = userService.getUserProfile(id);
        return ResponseEntity.ok(userProfile);
    }


}
