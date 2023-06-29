package cl.drcde.cqrs.application.command;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.service.UserService;
import cl.drcde.cqrs.domain.shared.commandbus.CommandHandler;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "CreateUserCommandHandler")
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {

    private final UserService userService;


    public CreateUserCommandHandler(UserService userService) {
        this.userService = userService;
    }


    // Constructor
    @Transactional
    public void handle(CreateUserCommand command) {
        User user = new User(new Username(command.getUsername()), new Password(command.getPassword()));
        this.userService.createUser(user);
//        this.eventBus.publish(user);
    }
}
