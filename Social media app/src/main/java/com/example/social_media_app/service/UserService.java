package com.example.social_media_app.service;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.UserRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{


    private final UserRepository userRepository;

    ModelMapper mapper = new ModelMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).get();
//        user.setUsername(updatedUser.getUsername());
//        user.setBio(updatedUser.getBio());
//        user.setEmail(updatedUser.getEmail());
//        user.setProfilePicture(updatedUser.getProfilePicture());
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(updatedUser , user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
       userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
