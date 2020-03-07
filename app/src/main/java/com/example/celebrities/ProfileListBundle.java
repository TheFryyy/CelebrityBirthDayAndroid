package com.example.celebrities;

import java.io.Serializable;
import java.util.List;

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
