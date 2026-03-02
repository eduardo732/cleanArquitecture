package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test: FindAllUsersQueryHandler
 *
 * ✅ VENTAJA: Tests muy rápidos (sin BD real)
 * ✅ VENTAJA: Aislados e independientes
 * ✅ VENTAJA: Sin estado compartido
 */
@RunWith(MockitoJUnitRunner.class)
public class FindAllUsersQueryHandlerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindAllUsersQueryHandler handler;

    @Test
    public void testFindAllUsers_Success() {
        // ARRANGE
        UserId userId1 = UserId.generate();
        UserId userId2 = UserId.generate();

        User user1 = new User(userId1, new Username("john"), new Password("pass123"));
        User user2 = new User(userId2, new Username("jane"), new Password("pass456"));

        List<User> mockUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(mockUsers);

        FindAllUsersQuery query = new FindAllUsersQuery();

        // ACT
        List<User> result = handler.handle(query);

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllUsers_Empty() {
        // ARRANGE
        List<User> mockUsers = Arrays.asList();
        when(userRepository.findAll()).thenReturn(mockUsers);

        FindAllUsersQuery query = new FindAllUsersQuery();

        // ACT
        List<User> result = handler.handle(query);

        // ASSERT
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository, times(1)).findAll();
    }
}
