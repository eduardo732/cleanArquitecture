package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.Username;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test: FindByIdQueryHandler
 *
 * VENTAJA: Tests muy rápidos (sin BD real)
 * VENTAJA: Mock del repositorio
 * VENTAJA: Completamente aislado
 */
@RunWith(MockitoJUnitRunner.class)
public class FindByIdQueryHandlerTest {

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String USERNAME = "john_doe";
    private static final String PASSWORD = "SecurePass123!";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindByIdQueryHandler handler;

    @Test
    public void testFindByIdQuery_Success() {
        // ARRANGE
        UserId userId = new UserId(USER_ID);
        User mockUser = new User(userId, new Username(USERNAME), new Password(PASSWORD));

        when(userRepository.findById(any(UUIDv4.class))).thenReturn(mockUser);

        FindByIdQuery query = new FindByIdQuery(USER_ID);

        // ACT
        User result = handler.handle(query);

        // ASSERT
        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername().value());
        assertEquals(PASSWORD, result.getPassword().value());
        verify(userRepository, times(1)).findById(any(UUIDv4.class));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindByIdQuery_NotFound() {
        // ARRANGE
        when(userRepository.findById(any(UUIDv4.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        FindByIdQuery query = new FindByIdQuery(USER_ID);

        // ACT & ASSERT
        handler.handle(query);
        // Debe lanzar EntityNotFoundException
    }
}

