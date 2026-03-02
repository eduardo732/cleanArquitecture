package cl.drcde.cqrs.presentation.query;

import cl.drcde.cqrs.application.query.FindAllUsersQuery;
import cl.drcde.cqrs.application.query.FindByIdQuery;
import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.shared.querybus.QueryBus;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import cl.drcde.cqrs.presentation.shared.ApiResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test: FindAllUsersController
 *
 * ✅ VENTAJA: Tests del endpoint HTTP
 * ✅ VENTAJA: Mock del QueryBus
 * ✅ VENTAJA: Sin servidor HTTP real
 */
@RunWith(MockitoJUnitRunner.class)
public class FindAllUsersControllerTest {

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String USERNAME = "john_doe";
    private static final String PASSWORD = "SecurePass123!";

    @Mock
    private QueryBus queryBus;

    @InjectMocks
    private FindAllUsersController controller;

    @Test
    public void testGetAll_Success() {
        // ARRANGE
        UserId userId = new UserId(USER_ID);
        User mockUser = new User(userId, new Username(USERNAME), new Password(PASSWORD));
        List<User> mockUsers = Arrays.asList(mockUser);

        when(queryBus.handle(any(FindAllUsersQuery.class))).thenReturn(mockUsers);

        // ACT
        ResponseEntity<ApiResponse<List<User>>> response = controller.getAll();

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
        verify(queryBus, times(1)).handle(any(FindAllUsersQuery.class));
    }

    @Test
    public void testGetById_Success() {
        // ARRANGE
        UserId userId = new UserId(USER_ID);
        User mockUser = new User(userId, new Username(USERNAME), new Password(PASSWORD));

        when(queryBus.handle(any(FindByIdQuery.class))).thenReturn(mockUser);

        // ACT
        ResponseEntity<ApiResponse<User>> response = controller.getById(USER_ID);

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USERNAME, response.getBody().getData().getUsername().value());
        verify(queryBus, times(1)).handle(any(FindByIdQuery.class));
    }

    @Test
    public void testGetById_NotFound() {
        // ARRANGE
        when(queryBus.handle(any(FindByIdQuery.class)))
                .thenThrow(new RuntimeException("User not found"));

        // ACT
        ResponseEntity<ApiResponse<User>> response = controller.getById(USER_ID);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(queryBus, times(1)).handle(any(FindByIdQuery.class));
    }
}
