package com.example.social_media_app.service;

import com.example.social_media_app.Dto.CommentDTO;
import com.example.social_media_app.models.Comment;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.CommentRepository;
import com.example.social_media_app.repository.PostRepository;
import com.example.social_media_app.repository.UserRepository;
import com.example.social_media_app.security.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!AuthenticationUtils.isCurrentUser(user.getId())) {
            throw new AccessDeniedException("You can only create comments as yourself");
        }

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setUser(user);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentDTO.class);
    }
    @Override
    @Transactional
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        boolean isOwner = AuthenticationUtils.isCurrentUser(comment.getUser().getId());
        boolean isAdmin = AuthenticationUtils.isCurrentUserAdmin();

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only update your own comments");
        }

        comment.setContent(commentDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }
    @Override
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));


        boolean isOwner = AuthenticationUtils.isCurrentUser(comment.getUser().getId());
        boolean isAdmin = AuthenticationUtils.isCurrentUserAdmin();

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only delete your own comments or comments as an admin");
        }

        commentRepository.delete(comment);
    }
    @Override
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCommentsByPost(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

    @Transactional
    public void deleteCommentsByUser(Long userId) {
        commentRepository.deleteByUserId(userId);
    }
}
