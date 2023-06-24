package cl.drcde.cqrs.application.command;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.service.UserService;
import cl.drcde.cqrs.domain.shared.commandbus.CommandHandler;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "CreateUserCommandHandler")
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {

    private final UserService userService;
    private final EventBus eventBus;

    public CreateUserCommandHandler(UserService userService, EventBus eventBus) {
        this.userService = userService;
        this.eventBus = eventBus;
    }


    // Constructor
    @Transactional
    public void handle(CreateUserCommand command) {
        User user = new User(command.getUsername(), command.getPassword());
        this.userService.createUser(user);
        this.eventBus.publish(user);
    }
}
