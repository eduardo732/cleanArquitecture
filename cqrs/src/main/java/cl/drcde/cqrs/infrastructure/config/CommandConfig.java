package cl.drcde.cqrs.infrastructure.config;

import cl.drcde.cqrs.application.command.CreateUserCommandHandler;
import cl.drcde.cqrs.domain.service.UserService;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("cl.drcde.cqrs.application.command")
public class CommandConfig {
    //TODO: Engorrosa forma de vincular los handlers con comandos, hay que buscar una forma más automatizada
    private final UserService userService;
    private final EventBus eventBus;

    public CommandConfig(UserService userService, EventBus eventBus) {
        this.userService = userService;
        this.eventBus = eventBus;
    }

    @Bean
    public CreateUserCommandHandler createUserHandler() {
        return new CreateUserCommandHandler(this.userService);
    }
}
