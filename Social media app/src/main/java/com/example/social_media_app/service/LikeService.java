package com.example.social_media_app.service;

import com.example.social_media_app.Dto.LikeDTO;
import com.example.social_media_app.Dto.UserDTO;
import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.LikeRepository;
import com.example.social_media_app.repository.PostRepository;
import com.example.social_media_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService implements ILikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public LikeDTO addLike(LikeDTO likeDTO) {
        User user = userRepository.findById(likeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(likeDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("User has already liked this post");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        Like savedLike = likeRepository.save(like);

        return new LikeDTO(savedLike.getId(), savedLike.getUser().getId(), savedLike.getPost().getId());
    }

    @Transactional
    public void removeLike(Long likeId, Long userId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        if (!like.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only remove your own likes");
        }

        likeRepository.delete(like);
    }

    public boolean hasUserLikedPost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return likeRepository.findByUserAndPost(user, post).isPresent();
    }

    public long getLikeCountForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return likeRepository.countByPost(post);
    }

    public List<UserDTO> getUsersWhoLikedPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<Like> likes = likeRepository.findByPost(post);
        return likes.stream()
                .map(like -> new UserDTO(like.getUser().getId(), like.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePostLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        likeRepository.deleteByPost(post);
    }

    @Transactional
    public void deleteLikesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        likeRepository.deleteByUser(user);
    }
}
