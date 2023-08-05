package by.teachmeskills.shop.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PasswordForm {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Неверный формат пароля! " +
            "Длина пароля должна быть не короче 8 символов. Пароль должен содержать как минимум одну цифру," +
            "одну заглавную букву, одну букву нижнего регистра, один специальный символ.")
    @NotBlank(message = "Поле должно быть заполнено!")
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Неверный формат пароля! " +
            "Длина пароля должна быть не короче 8 символов. Пароль должен содержать как минимум одну цифру," +
            "одну заглавную букву, одну букву нижнего регистра, один специальный символ.")
    @NotBlank(message = "Поле должно быть заполнено!")
    private String newPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Неверный формат пароля! " +
            "Длина пароля должна быть не короче 8 символов. Пароль должен содержать как минимум одну цифру," +
            "одну заглавную букву, одну букву нижнего регистра, один специальный символ.")
    @NotBlank(message = "Поле должно быть заполнено!")
    private String newPasswordRep;
}
