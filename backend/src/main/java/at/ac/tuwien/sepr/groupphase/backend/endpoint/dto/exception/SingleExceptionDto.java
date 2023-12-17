package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleExceptionDto {

    private String message;

    @NotNull
    private String uiError;

    @NotNull
    private String uiErrorSummary;
}
