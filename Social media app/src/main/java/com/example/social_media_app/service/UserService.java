package com.example.social_media_app.service;

import com.example.social_media_app.Dto.UserSearchResultDTO;
import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.UserRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{


    private final UserRepository userRepository;
    private final PostService postService;

    ModelMapper mapper = new ModelMapper();

    public UserService(UserRepository userRepository, PostService postService) {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    @Override
    public User createUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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

    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user.getId();
    }

    public List<UserSearchResultDTO> searchUsers(String searchTerm) {
        return userRepository.searchUsers(searchTerm).stream()
                .map(user -> new UserSearchResultDTO(user.getId(), user.getUsername(), user.getProfilePicture(), user.getBio()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getUserProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        List<Post> userPosts = postService.findPostsByUser(userId);
        return Map.of(
                "user", user,
                "posts", userPosts
        );
    }
}
