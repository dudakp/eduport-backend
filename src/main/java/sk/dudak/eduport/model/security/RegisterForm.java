package sk.dudak.eduport.model.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm implements Serializable {
    @NotEmpty
    private String username;
    private String firstName;
    private String lastName;
    @NotEmpty
    private String[] roles;
    @NotEmpty
    private String password;
}
