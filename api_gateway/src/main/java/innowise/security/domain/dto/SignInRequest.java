package innowise.security.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {

    @Size(min = 5, max = 50, message = "имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "имя пользователя не может быть пустыми")
    private String username;

    @Size(min = 8, max = 255, message = "длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "пароль не может быть пустыми")
    private String password;
}