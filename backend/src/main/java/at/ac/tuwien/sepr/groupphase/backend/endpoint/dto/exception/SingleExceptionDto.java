package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SingleExceptionDto {

    private String message;

    @NotNull
    private String uiError;
}
