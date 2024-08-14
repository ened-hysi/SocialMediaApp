package com.example.social_media_app.service;

import com.example.social_media_app.Dto.CommentDTO;

import java.util.List;

public interface ICommentService {

    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
    CommentDTO getCommentById(Long id);
    List<CommentDTO> getCommentsByPostId(Long postId);

}
