package cl.drcde.cqrs.presentation.query;

import cl.drcde.cqrs.application.query.FindAllUsersQuery;
import cl.drcde.cqrs.application.query.FindByIdQuery;
import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.shared.querybus.QueryBus;
import cl.drcde.cqrs.presentation.shared.ApiResponse;
import cl.drcde.cqrs.presentation.shared.Messages;
import cl.drcde.cqrs.presentation.shared.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller: Endpoints de consulta (GET)
 *
 * VENTAJA: Minimalista, solo mapea HTTP a Query
 * VENTAJA: Sin logica de negocio
 * VENTAJA: QueryBus maneja la orquestacion
 */
@Slf4j
@RestController
@RequestMapping(Routes.User.PREFIX)
public class FindAllUsersController {

    private final QueryBus queryBus;

    public FindAllUsersController(QueryBus queryBus) {
        this.queryBus = queryBus;
    }

    /**
     * Endpoint: GET /users
     *
     * Obtiene todos los usuarios
     *
     * FLUJO:
     * 1. GET /users
     * 2. Crear FindAllUsersQuery()
     * 3. QueryBus.handle(query)
     * 4. FindAllUsersQueryHandler.handle()
     * 5. Retornar lista de usuarios
     *
     * @return ResponseEntity con lista de usuarios
     */
    @GetMapping(value = Routes.User.GET)
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        try {
            log.info("GET /users - Obteniendo todos los usuarios");

            // 1. Crear la query
            FindAllUsersQuery query = new FindAllUsersQuery();

            // 2. Ejecutar a través del QueryBus
            List<User> users = this.queryBus.handle(query);

            // 3. Respuesta exitosa
            ApiResponse<List<User>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    users,
                    Messages.SUCCESS
            );

            log.info("Se retornaron {} usuarios", users.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener usuarios", e);
            ApiResponse<List<User>> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    Messages.INTERNAL_SERVER_ERROR
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * Endpoint: GET /users/{id}
     *
     * Obtiene un usuario específico por ID
     *
     * FLUJO:
     * 1. GET /users/{id}
     * 2. Crear FindByIdQuery(id)
     * 3. QueryBus.handle(query)
     * 4. FindByIdQueryHandler.handle()
     * 5. Retornar usuario encontrado
     *
     * @param id ID del usuario a buscar (UUID)
     * @return ResponseEntity con el usuario
     */
    @GetMapping(value = Routes.User.USER_ID)
    public ResponseEntity<ApiResponse<User>> getById(
            @PathVariable(name = "id") String id
    ) {
        try {
            log.info("GET /users/{} - Obteniendo usuario por ID", id);

            // 1. Crear la query con el ID
            FindByIdQuery query = new FindByIdQuery(id);

            // 2. Ejecutar a traves del QueryBus
            User user = this.queryBus.handle(query);

            // 3. Respuesta exitosa
            ApiResponse<User> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    user,
                    Messages.SUCCESS
            );

            log.info("Usuario encontrado: {}", user.getUsername().value());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener usuario con ID: {}", id, e);
            ApiResponse<User> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    "Usuario no encontrado"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

