package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BlockedIpExceptionDto extends SingleExceptionDto {
    private long timeout;
}
