package cl.drcde.cqrs.presentation.command;

import cl.drcde.cqrs.application.command.CreateUserCommand;
import cl.drcde.cqrs.domain.shared.commandbus.CommandBus;
import cl.drcde.cqrs.domain.shared.exception.DomainException;
import cl.drcde.cqrs.presentation.dto.CreateUserDto;
import cl.drcde.cqrs.presentation.shared.Messages;
import cl.drcde.cqrs.presentation.shared.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@EnableAutoConfiguration
public final class CreateUserController {
   private final CommandBus commandBus;

    public CreateUserController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }
    //TODO: revisar que falta para tener un buen template
    //TODO: revisar en cada capa que logs poner, revisar respuesta generica
    //TODO: implementar lógica para encriptar y guardar contraseñas seguras
    //TODO: implementar query para ver a los usuarios creados
    //TODO: hacer Testing de las capas completas
    //TODO: investigar sobre jwt para el consumo de la api

    @PostMapping(value = Routes.User.POST)
    public ResponseEntity<String> post(@RequestBody CreateUserDto dto) {
        log.info("[User] CreateUserController::post dto: {}", dto);
        HttpStatus httpStatus;
        String body;
        try {
            this.commandBus.handle(
                    new CreateUserCommand(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );
            httpStatus = HttpStatus.CREATED;
            body = Messages.CREATED;
        } catch (DomainException exception) {
            httpStatus = HttpStatus.BAD_REQUEST;
            body = Messages.BAD_REQUEST;
            log.warn(exception.getMessage(), exception);
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            body = Messages.INTERNAL_SERVER_ERROR;
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.status(httpStatus).body(body);
    }
}
