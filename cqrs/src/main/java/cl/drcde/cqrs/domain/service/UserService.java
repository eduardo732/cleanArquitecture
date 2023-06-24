package cl.drcde.cqrs.domain.service;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        //Aquí va la lógica de negocio
        return this.userRepository.save(user);
    }
}
