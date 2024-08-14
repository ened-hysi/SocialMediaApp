package com.example.social_media_app.repository;

import com.example.social_media_app.models.Follow;
import com.example.social_media_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowed(User followed);
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);
}
