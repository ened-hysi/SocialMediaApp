package com.example.social_media_app.service;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User createUser(User user);

    Optional<User> findById(Long id);

    User updateUser(Long id,User updatedUser);

    void deleteUser(Long id);

    List<User> getAllUsers();

}
