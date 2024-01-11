package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

public record PasswordResetDto(
    String password,
    String token
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PasswordResetDto{password=");
        if (password == null || password.isEmpty()) {
            sb.append(password);
        } else {
            sb.append("*");
        }
        sb.append(", token=");
        sb.append(token);
        sb.append("}");
        return sb.toString();
    }
}
