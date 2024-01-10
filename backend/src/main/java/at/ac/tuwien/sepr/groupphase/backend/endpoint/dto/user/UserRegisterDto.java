package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

import lombok.NonNull;

@NonNull
public record UserRegisterDto(
    String email,
    String password,
    String lang
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UserRegisterDto{email=" + email + ", password=");
        if (password == null || password.isEmpty()) {
            sb.append(password);
        } else {
            sb.append("*");
        }
        sb.append(", lang=");
        sb.append(lang);
        sb.append("}");
        return sb.toString();
    }
}
