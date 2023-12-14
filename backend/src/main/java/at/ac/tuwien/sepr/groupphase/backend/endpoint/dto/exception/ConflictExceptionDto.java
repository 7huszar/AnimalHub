package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class ConflictExceptionDto {
    private String enSummary;
    private String deSummary;
    private String[] enErr;
    private String[] deErr;
}
