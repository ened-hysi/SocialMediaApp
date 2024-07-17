package com.example.social_media_app.service;

import com.example.social_media_app.Dto.CommentDto;
import com.example.social_media_app.models.Comment;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;

import java.util.Optional;

public interface ICommentService {

    Comment createComment(CommentDto commentDto, User user, Post post);

    Optional<Comment> findCommentById(Long id);

    Comment updateComment(Long id ,Comment comment);

    void deleteComment(Long id);

}
