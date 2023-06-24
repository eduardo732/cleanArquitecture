package cl.drcde.cqrs.application.command;

import cl.drcde.cqrs.domain.shared.commandbus.Command;
import lombok.NonNull;

public final class CreateUserCommand extends Command {
    private final @NonNull String username;
    private final @NonNull String password;

    public CreateUserCommand(
            @NonNull String username,
            @NonNull String password
    ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

}
