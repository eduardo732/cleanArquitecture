package cl.drcde.cqrs.infrastructure.persistence;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.Username;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserRepositoryImplTest {
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    private JpaUserRepository jpaUserRepository;
    private UserRepositoryImpl userRepositoryImpl;
    @Before
    public void setUp() {
        this.jpaUserRepository = mock(JpaUserRepository.class);
        this.userRepositoryImpl = new UserRepositoryImpl(this.jpaUserRepository);
    }
    @Test
    public void saveUserTest() {
        UUIDv4 userId = UserId.generate();
        User user = new User(userId, new Username(USERNAME), new Password(PASSWORD));
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        when(this.jpaUserRepository.save(any(JpaUser.class))).thenReturn(jpaUser);
        User createdUser = this.userRepositoryImpl.save(user);
        verify(jpaUserRepository).save(any(JpaUser.class));
        assertEquals(createdUser.getId(), jpaUser.getId());
    }
    @Test
    public void findByIdTest() {
        UUIDv4 id = UUIDv4.generate();
        User user = new User(id, new Username(USERNAME), new Password(PASSWORD));
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        when(this.jpaUserRepository.findById(id)).thenReturn(Optional.of(jpaUser));
        User foundUser = this.userRepositoryImpl.findById(id);
        verify(jpaUserRepository).findById(id);
        assertEquals(id, foundUser.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void notFoundTest() {
        UUIDv4 id = UUIDv4.generate();
        User user = new User(id, new Username(USERNAME), new Password(PASSWORD));
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        when(this.jpaUserRepository.findById(id)).thenReturn(Optional.empty());
        this.userRepositoryImpl.findById(id);
    }

    @Test
    public void deleteTest() {
        UUIDv4 userId = UUIDv4.generate();
        User user = new User(userId, new Username(USERNAME), new Password(PASSWORD));
        JpaUser jpaUser = new JpaUser(user.getId(), user.getUsername(), user.getPassword());
        doNothing().when(this.jpaUserRepository).delete(jpaUser);
        userRepositoryImpl.delete(user);
        verify(this.jpaUserRepository).delete(jpaUser);
    }
}
