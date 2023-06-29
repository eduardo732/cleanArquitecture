package cl.drcde.cqrs.infrastructure.eventbus;

import cl.drcde.cqrs.domain.shared.commandbus.Command;
import cl.drcde.cqrs.domain.shared.commandbus.CommandHandler;
import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

public class SpringCommandBusTest {
    private ApplicationContext applicationContext;
    private SpringCommandBus springCommandBus;

    @Before
    public void setUp() {
        this.applicationContext = mock(ApplicationContext.class);
        this.springCommandBus = new SpringCommandBus(this.applicationContext);
    }

    @Test
    public void handleTest() throws CommandBusException {
        Command command = new Command();
        CommandHandler commandHandler = mock(CommandHandler.class);
        String handlerBeanName = command.getClass().getSimpleName() + "Handler";
        when(applicationContext.getBean(handlerBeanName, CommandHandler.class)).thenReturn(commandHandler);
        this.springCommandBus.handle(command);
        verify(applicationContext, times(1)).getBean(handlerBeanName, CommandHandler.class);
        verify(commandHandler, times(1)).handle(command);
    }
}
