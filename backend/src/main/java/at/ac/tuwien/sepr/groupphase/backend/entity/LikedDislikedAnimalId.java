package at.ac.tuwien.sepr.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikedDislikedAnimalId implements Serializable {

    private ApplicationUser user;

    private Animal animal;


}
