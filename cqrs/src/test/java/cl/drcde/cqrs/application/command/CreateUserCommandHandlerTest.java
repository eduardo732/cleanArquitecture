package cl.drcde.cqrs.application.command;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.service.UserService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CreateUserCommandHandlerTest {
    private UserService userService;
    private CreateUserCommandHandler createUserCommandHandler;
    @Before
    public void setUp() {
        this.userService = mock(UserService.class);
        this.createUserCommandHandler = new CreateUserCommandHandler(this.userService);
    }
    @Test
    public void handleTest() {
        CreateUserCommand command = new CreateUserCommand("TEST", "TEST");
        this.createUserCommandHandler.handle(command);
        verify(this.userService, times(1)).createUser(any(User.class));
    }
}
