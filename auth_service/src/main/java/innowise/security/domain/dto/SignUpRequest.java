package innowise.security.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Size(min = 5, max = 50, message = "имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "имя пользователя не может быть пустыми")
    private String username;

    @Size(min = 5, max = 255, message = "адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "адрес электронной почты не может быть пустыми")
    @Email(message = "email адрес должен быть в формате user@example.com")
    private String email;

    @Size(max = 255, message = "длина пароля должна быть не более 255 символов")
    private String password;
}