package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class ErrorListExceptionDto {

    /**
     * Describes what went wrong. This will be the title of the error message in the user interface. Should be the
     * id of translation in property files in frontend.
     */
    @NotNull
    private String uiErrorDescriptor;
    /**
     * List of errors from backend.
     */
    private List<String> errors;
    /**
     * Same as errors, but the id for the translation.
     */
    @NotNull
    private List<String> uiErrors;
}
