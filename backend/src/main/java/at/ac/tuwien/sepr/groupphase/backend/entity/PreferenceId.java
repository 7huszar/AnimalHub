package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.Species;

import java.io.Serializable;

public class PreferenceId implements Serializable {

    private ApplicationUser user;
    private Species species;

    public PreferenceId(ApplicationUser user, Species species) {
        this.user = user;
        this.species = species;
    }

}
