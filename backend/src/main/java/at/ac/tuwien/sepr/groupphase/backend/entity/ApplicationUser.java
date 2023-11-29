package at.ac.tuwien.sepr.groupphase.backend.entity;



import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//TODO: replace this class with a correct ApplicationUser Entity implementation
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {

    private String email;
    private String password;
    private UserRole userRole;
}
