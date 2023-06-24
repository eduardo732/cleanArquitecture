package cl.drcde.cqrs.presentation.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
