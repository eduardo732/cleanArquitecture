package cl.drcde.cqrs.domain.service;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    public static final String USERNAME = "test";
    public static final String PASSWORD = "test";
    private UserService userService;
    private UserRepository userRepository;
    @Before
    public void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.userService = new UserService(this.userRepository);
    }
    @Test
    public void createUserTest() {
        User user = new User(new Username(USERNAME), new Password(PASSWORD));
        when(this.userRepository.save(user)).thenReturn(user);
        User createdUser = this.userService.createUser(user);
        verify(this.userRepository, times(1)).save(user);
        assertEquals(user, createdUser);
    }
}
