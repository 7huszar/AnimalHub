package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;

import java.io.Serializable;

public class LikedDislikedAnimalId implements Serializable {

    private ApplicationUser user;
    private Animal animal;

    public LikedDislikedAnimalId(ApplicationUser user, Animal animal) {
        this.user = user;
        this.animal = animal;
    }

}
