package com.example.celebrities.utils;

import com.example.celebrities.model.Profile;

import java.io.Serializable;
import java.util.List;


/* *****  This class permits to pass a list of profiles between activities  ***** */
public class ProfileListBundle implements Serializable {
    private List<Profile> profiles;

    public ProfileListBundle(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
