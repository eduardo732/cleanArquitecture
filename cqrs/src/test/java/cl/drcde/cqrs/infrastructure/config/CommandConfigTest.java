package cl.drcde.cqrs.infrastructure.config;

import cl.drcde.cqrs.application.command.CreateUserCommandHandler;
import cl.drcde.cqrs.domain.service.UserService;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class CommandConfigTest {
    private UserService userService;
    private EventBus eventBus;
    private CommandConfig commandConfig;
    @Before
    public void setUp() {
        this.userService = mock(UserService.class);
        this.eventBus = mock(EventBus.class);
        this.commandConfig = new CommandConfig(this.userService, this.eventBus);
    }
    @Test
    public void createUserHandlerTest() {
        CreateUserCommandHandler createUserCommandHandler = this.commandConfig.createUserHandler();
        assertNotNull(createUserCommandHandler);
    }
}
