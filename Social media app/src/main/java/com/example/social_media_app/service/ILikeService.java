package com.example.social_media_app.service;

import com.example.social_media_app.Dto.LikeDTO;
import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;

public interface ILikeService {

    LikeDTO addLike(LikeDTO likeDTO);
    void removeLike(Long likeId, Long userId);
}
