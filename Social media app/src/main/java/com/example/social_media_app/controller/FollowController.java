package com.example.social_media_app.controller;

import com.example.social_media_app.Dto.FollowerDTO;
import com.example.social_media_app.models.User;
import com.example.social_media_app.security.AuthenticationUtils;
import com.example.social_media_app.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }


    @PostMapping("/{userId}")
    public ResponseEntity<Void> followUser(@PathVariable Long userId) {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        followService.followUser(currentUserId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId) {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        followService.unfollowUser(currentUserId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowerDTO>> getFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowers(userId);
        List<FollowerDTO> followerDTOs = followers.stream()
                .map(FollowerDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(followerDTOs);
    }


    @GetMapping("/following/{userId}")
    public ResponseEntity<List<FollowerDTO>> getFollowing(@PathVariable Long userId) {
        List<User> following = followService.getFollowing(userId);
        List<FollowerDTO> followingDTOs = following.stream()
                .map(FollowerDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(followingDTOs);
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long userId) {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(followService.isFollowing(currentUserId, userId));
    }

}
