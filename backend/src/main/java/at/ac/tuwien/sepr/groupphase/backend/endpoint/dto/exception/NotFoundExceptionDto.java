package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception;

import at.ac.tuwien.sepr.groupphase.backend.enums.NotFoundType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundExceptionDto {
    private NotFoundType type;
    private String[] info;
}
