package com.example.social_media_app.service;

import com.example.social_media_app.models.Like;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService implements ILikeService {

    private final  LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public Like addLike(User user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        return likeRepository.save(like);
    }

    @Override
    public void removeLike(Long id) {
         likeRepository.deleteById(id);
    }
}
