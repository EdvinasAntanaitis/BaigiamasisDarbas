package lt.code.samples.maven.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lt.code.samples.maven.common.validation.annotation.fieldsstringcompare.FieldsStringCompare;


@Builder

@FieldsStringCompare(
        firstField = "password",
        secondField = "passwordRepeat",
        message = "{user.signup.fields.not.match}")
public record UserSignUpDto(@NotBlank String name,
                            @NotBlank String surname,
                            @NotBlank String email,
                            @NotBlank String username,
                             String phoneNumber,
                            @NotBlank String password,
                            @NotBlank String passwordRepeat,
                            @NotBlank String role
) {}

