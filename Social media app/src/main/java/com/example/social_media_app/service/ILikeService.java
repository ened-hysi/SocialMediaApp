package com.example.social_media_app.service;

import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;

public interface ILikeService {

    Like addLike(User user , Post post);

    void removeLike(Long id);
}
