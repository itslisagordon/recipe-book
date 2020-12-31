package org.liftoff.recipebook.models.dto;

import javax.validation.constraints.Size;

public class EditProfileDTO {

    private String profilePicture;

    @Size(min = 3, max = 300, message = "Bio must be between 3 and 300 characters.")
    private String bio;

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


}
