package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class MessageInquiryDto {

    @NotNull(message = "Title must not be null")
    @Size(max = 100)
    private String title;

    @NotNull(message = "Summary must not be null")
    @Size(max = 500)
    private String summary;

    @NotNull(message = "Text must not be null")
    @Size(max = 10000)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageInquiryDto that)) {
            return false;
        }
        return Objects.equals(title, that.title)
            && Objects.equals(summary, that.summary)
            && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, summary, text);
    }

    @Override
    public String toString() {
        return "MessageInquiryDto{"
            + "title='" + title + '\''
            + ", summary='" + summary + '\''
            + ", text='" + text + '\''
            + '}';
    }
}