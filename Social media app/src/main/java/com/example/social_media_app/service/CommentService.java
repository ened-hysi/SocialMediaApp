package com.example.social_media_app.service;

import com.example.social_media_app.Dto.CommentDto;
import com.example.social_media_app.models.Comment;
import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.CommentRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    ModelMapper mapper = new ModelMapper();

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment createComment(CommentDto commentDto , User user, Post post) {
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setUser(user);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment updateComment(Long id, Comment comment) {
        Comment commentU = commentRepository.findById(id).get();
//        commentU.setContent(comment.getContent());
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(comment , commentU);
        return commentRepository.save(commentU);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
