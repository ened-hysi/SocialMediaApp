package com.example.social_media_app.controller;

import com.example.social_media_app.Dto.LikeDTO;
import com.example.social_media_app.Dto.UserDTO;
import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.LikeRepository;
import com.example.social_media_app.security.AuthenticationUtils;
import com.example.social_media_app.service.LikeService;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;


    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;

    }

    @PostMapping("/{postId}")
    public ResponseEntity<LikeDTO> addLike(@PathVariable Long postId) {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        LikeDTO likeDTO = new LikeDTO(null, currentUserId, postId);
        LikeDTO addedLike = likeService.addLike(likeDTO);
        return new ResponseEntity<>(addedLike, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id) {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        likeService.removeLike(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Boolean> hasUserLikedPost(@PathVariable Long postId) {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        boolean hasLiked = likeService.hasUserLikedPost(currentUserId, postId);
        return ResponseEntity.ok(hasLiked);
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> getLikeCountForPost(@PathVariable Long postId) {
        long likeCount = likeService.getLikeCountForPost(postId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/users/{postId}")
    public ResponseEntity<List<UserDTO>> getUsersWhoLikedPost(@PathVariable Long postId) {
        List<UserDTO> users = likeService.getUsersWhoLikedPost(postId);
        return ResponseEntity.ok(users);
    }
}
