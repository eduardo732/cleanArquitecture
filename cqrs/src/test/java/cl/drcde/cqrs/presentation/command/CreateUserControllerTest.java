package cl.drcde.cqrs.presentation.command;

import cl.drcde.cqrs.application.command.CreateUserCommand;
import cl.drcde.cqrs.domain.shared.commandbus.CommandBus;
import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;
import cl.drcde.cqrs.domain.shared.exception.DomainException;
import cl.drcde.cqrs.presentation.dto.CreateUserDto;
import cl.drcde.cqrs.presentation.shared.ApiResponse;
import cl.drcde.cqrs.presentation.shared.Messages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateUserControllerTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    @Mock
    private CommandBus commandBus;

    @InjectMocks
    private CreateUserController createUserController;

    @Test
    public void testPost_Successful() throws CommandBusException {
        // Arrange
        CreateUserDto dto = new CreateUserDto();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);

        // Act
        ResponseEntity<ApiResponse<String>> response = createUserController.post(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
        assertEquals(Messages.CREATED, response.getBody().getData());
        verify(commandBus, times(1)).handle(any(CreateUserCommand.class));
    }

    @Test
    public void testPost_DomainException() throws CommandBusException {
        // Arrange
        CreateUserDto dto = new CreateUserDto();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);

        doThrow(new DomainException("Domain exception message")).when(commandBus).handle(any(CreateUserCommand.class));

        // Act
        ResponseEntity<ApiResponse<String>> response = createUserController.post(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(Messages.BAD_REQUEST, response.getBody().getData());
        verify(commandBus, times(1)).handle(any(CreateUserCommand.class));
    }

    @Test
    public void testPost_Exception() throws CommandBusException {
        // Arrange
        CreateUserDto dto = new CreateUserDto();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);

        doThrow(new RuntimeException("Unexpected exception")).when(commandBus).handle(any(CreateUserCommand.class));

        // Act
        ResponseEntity<ApiResponse<String>> response = createUserController.post(dto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals(Messages.INTERNAL_SERVER_ERROR, response.getBody().getData());
        verify(commandBus, times(1)).handle(any(CreateUserCommand.class));
    }
}
