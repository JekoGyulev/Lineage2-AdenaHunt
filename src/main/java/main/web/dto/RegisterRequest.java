package main.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    @Size(min = 4, max = 16)
    private String username;
    @NotBlank
    @Size(min = 4, max = 16)
    private String password;
    @NotBlank
    @Size(min = 4, max = 16)
    private String nickname;
}
