package com.example.social_media_app.Dto;

public class UserSearchResultDTO {


    private Long id;
    private String username;
    private String profilePicture;
    private String bio;

    public UserSearchResultDTO(Long id, String username, String profilePicture, String bio) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


}