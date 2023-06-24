package cl.drcde.cqrs.infrastructure.eventbus;

import cl.drcde.cqrs.domain.shared.commandbus.Command;
import cl.drcde.cqrs.domain.shared.commandbus.CommandBus;
import cl.drcde.cqrs.domain.shared.commandbus.CommandHandler;
import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("SpringCommandBus")
@Primary
@Slf4j
public final class SpringCommandBus extends BaseBus implements CommandBus {
    private final ApplicationContext applicationContext;

    public SpringCommandBus(
            ApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;
    }

    private String getCommandClass(CommandHandler commandHandler) {
        log.debug("SpringCommandBus::getCommandClass command {}", commandHandler);
        log.debug("SpringCommandBus::getCommandClass commandClass {}", commandHandler.getClass().getSimpleName());
        return commandHandler.getClass().getSimpleName();
    }

    @Override
    public void handle(Command command) throws CommandBusException {
        String commandName = command.getClass().getSimpleName();
        String handlerBeanName = commandName + "Handler";
        CommandHandler commandHandler = applicationContext.getBean(handlerBeanName, CommandHandler.class);
        commandHandler.handle(command);
    }
}
