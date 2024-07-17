package com.example.social_media_app.service;

import com.example.social_media_app.models.Post;
import com.example.social_media_app.models.User;
import com.example.social_media_app.repository.PostRepository;
import com.example.social_media_app.repository.UserRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    ModelMapper mapper = new ModelMapper();

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;

        this.userRepository = userRepository;
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post updatePost(Long id ,Post updatedPost) {
        Post post = postRepository.findById(id).get();
//        post.setContent(updatedpost.getContent());
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(updatedPost , post);
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
       postRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findPostsByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return postRepository.findByUser(user);
    }


}
