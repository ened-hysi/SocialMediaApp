package com.example.social_media_app.service;

import com.example.social_media_app.models.User;

import java.util.List;

    public interface IFollowService {
        void followUser(Long followerId, Long followedId);

        void unfollowUser(Long followerId, Long followedId);

        List<User> getFollowers(Long userId);

        List<User> getFollowing(Long userId);

        boolean isFollowing(Long followerId, Long followedId);
    }

