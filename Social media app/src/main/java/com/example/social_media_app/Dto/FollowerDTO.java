package com.example.social_media_app.Dto;

import com.example.social_media_app.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerDTO {
    private Long id;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
    private String authority;

    public FollowerDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profilePicture = user.getProfilePicture();
        this.bio = user.getBio();
        this.authority = user.getAuthority();
    }
}
