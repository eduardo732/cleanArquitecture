# Ejemplos Prácticos: CQRS + Clean Architecture

## 1. Value Object: Username

```java
package cl.drcde.cqrs.domain.vo;

import cl.drcde.cqrs.domain.shared.exception.DomainException;
import lombok.NonNull;

public final class Username implements ValueObject {
    public static final int LENGTH = 200;
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 500;
    
    private final @NonNull String _value;

    /**
     * Constructor con validación
     * 
     * ✅ VENTAJA: La validación ocurre UNA SOLA VEZ
     * ✅ VENTAJA: Username siempre es válido
     * ✅ VENTAJA: Reutilizable en todo el código
     */
    public Username(@NonNull String value) {
        // Validar no vacío
        if(value.trim().isEmpty()) 
            throw new DomainException("Username cannot be empty");
        
        // Validar longitud
        int length = value.length();
        if(length < MIN_LENGTH || length > MAX_LENGTH) 
            throw new DomainException(
                "Username must be between " + MIN_LENGTH + " and " + MAX_LENGTH
            );
        
        _value = value;
    }

    @Override
    public String value() {
        return this._value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Username)) return false;
        Username that = (Username) other;
        return _value.equals(that._value);
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }

    @Override
    public String toString() {
        return _value;
    }
}
```

**Por qué es importante:**
- ✅ Una sola responsabilidad: validar username
- ✅ Imposible tener un Username inválido
- ✅ Código cliente no necesita validar

---

## 2. Aggregate Root: User (Lógica de Negocio)

```java
package cl.drcde.cqrs.domain.model;

import cl.drcde.cqrs.domain.events.CreatedUserEvent;
import cl.drcde.cqrs.domain.shared.aggregateroot.AggregateRoot;
import cl.drcde.cqrs.domain.shared.eventbus.EventCollection;
import cl.drcde.cqrs.domain.shared.model.BaseModel;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;

/**
 * Aggregate Root: User
 * 
 * ✅ VENTAJA: Contiene toda la lógica de negocio de usuarios
 * ✅ VENTAJA: Sin dependencias de BD, HTTP, etc
 * ✅ VENTAJA: Testeable sin mocks
 * ✅ VENTAJA: Genera eventos automáticamente
 */
public class User extends BaseModel implements AggregateRoot {
    private Username username;
    private Password password;
    private final EventCollection events = new EventCollection();

    /**
     * Constructor para crear nuevo usuario
     */
    public User(
            UserId id,
            Username username,
            Password password
    ) {
        super(id);
        this.username = username;
        this.password = password;
        
        // ✅ Generar evento cuando se crea
        this.getEvents().add(
                new CreatedUserEvent(
                        id,
                        this.username,
                        this.password
                )
        );
    }

    /**
     * Getters: Solo lectura, sin mutabilidad
     */
    public Username getUsername() {
        return this.username;
    }

    public Password getPassword() {
        return this.password;
    }

    /**
     * Eventos generados por este Aggregate
     */
    @Override
    public EventCollection getEvents() {
        return this.events;
    }
}
```

**Por qué es importante:**
- ✅ Toda la lógica está aquí, no dispersa
- ✅ Los eventos se generan automáticamente
- ✅ No conoce de BD ni HTTP

---

## 3. Command: CreateUserCommand

```java
package cl.drcde.cqrs.application.command;

/**
 * Command: Intención de cambiar el estado
 * 
 * ✅ VENTAJA: Inmutable
 * ✅ VENTAJA: Representa una acción clara
 * ✅ VENTAJA: Fácil de loguear y auditar
 */
public final class CreateUserCommand {
    private final String username;
    private final String password;

    public CreateUserCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
```

**Vs DTO:**
```
CreateUserDto (HTTP)  ←→  CreateUserCommand (Aplicación)
- Json                    - Java puro
- Con validadores JPA     - Sin dependencias de web
- Puede ser null          - Inmutable
```

---

## 4. CommandHandler: CreateUserCommandHandler

```java
package cl.drcde.cqrs.application.command;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.shared.commandbus.CommandHandler;
import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import org.springframework.stereotype.Component;

/**
 * CommandHandler: Orquesta la lógica del comando
 * 
 * FLUJO:
 * 1. Crear ValueObjects (validación)
 * 2. Crear Aggregate (lógica de negocio)
 * 3. Persistir
 * 4. Publicar eventos
 */
@Component
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {
    
    private final UserRepository userRepository;
    private final EventBus eventBus;

    public CreateUserCommandHandler(
            UserRepository userRepository,
            EventBus eventBus
    ) {
        this.userRepository = userRepository;
        this.eventBus = eventBus;
    }

    /**
     * Ejecuta el comando
     * 
     * ✅ Manejo de errores de dominio
     * ✅ Persistencia
     * ✅ Publicación de eventos
     */
    @Override
    public void handle(CreateUserCommand command) throws CommandBusException {
        try {
            // 1️⃣ Validar y crear Value Objects
            Username username = new Username(command.getUsername());
            Password password = new Password(command.getPassword());

            // 2️⃣ Generar ID único
            UserId userId = UserId.generate();

            // 3️⃣ Crear el agregado (User generará eventos)
            User user = new User(userId, username, password);

            // 4️⃣ Persistir
            this.userRepository.save(user);

            // 5️⃣ Publicar eventos
            user.getEvents().forEach(this.eventBus::publish);

        } catch (Exception e) {
            throw new CommandBusException(e.getMessage());
        }
    }
}
```

**Separación de responsabilidades:**
```
Controller    → Maneja HTTP
DTO           → Mapea JSON
Command       → Intención
CommandHandler→ Orquestación
Domain        → Lógica pura
```

---

## 5. Query: FindAllUsersQuery

```java
package cl.drcde.cqrs.application.query;

/**
 * Query: Intención de leer datos
 * 
 * ✅ VENTAJA: Completamente separada de escrituras
 * ✅ VENTAJA: Puede ser optimizada independientemente
 * ✅ VENTAJA: No genera eventos
 */
public final class FindAllUsersQuery {
    // Esta query no necesita parámetros en este caso
    // Pero podría tener filtros, paginación, etc
    
    public FindAllUsersQuery() {
    }
}
```

---

## 6. QueryHandler: FindAllUsersQueryHandler

```java
package cl.drcde.cqrs.application.query;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.shared.querybus.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * QueryHandler: Maneja lectura de datos
 * 
 * ✅ VENTAJA: No afecta a la lógica de escritura
 * ✅ VENTAJA: Puede caché/optimizar sin límites
 * ✅ VENTAJA: No genera eventos
 */
@Component
public class FindAllUsersQueryHandler implements QueryHandler<FindAllUsersQuery, List<User>> {
    
    private final UserRepository userRepository;

    public FindAllUsersQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(FindAllUsersQuery query) {
        // ✅ Aquí podrías usar:
        // - Redis para cache
        // - Elasticsearch para búsqueda
        // - Diferentes réplicas de BD
        // - Sin afectar commands
        
        return this.userRepository.findAll();
    }
}
```

**Diferencias Command vs Query:**
```
Command (Escritura)        Query (Lectura)
- Genera eventos           - No genera eventos
- Transaccional            - Solo lectura
- Lenta es OK              - Debe ser rápida
- Una BD                   - Muchas BDs posibles
- Consistencia fuerte      - Consistencia eventual OK
```

---

## 7. Event: CreatedUserEvent

```java
package cl.drcde.cqrs.domain.events;

import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.shared.eventbus.Event;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;

import java.time.LocalDateTime;

/**
 * Event: Algo que sucedió en el dominio
 * 
 * ✅ VENTAJA: Auditoría completa
 * ✅ VENTAJA: Otros servicios pueden escuchar
 * ✅ VENTAJA: Event Sourcing
 * ✅ VENTAJA: Debugging histórico
 */
public class CreatedUserEvent extends Event {
    private final UserId userId;
    private final Username username;
    private final Password password;

    public CreatedUserEvent(
            UserId userId,
            Username username,
            Password password
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.occurredAt = LocalDateTime.now();
    }

    public UserId getUserId() {
        return userId;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    @Override
    public String getEventName() {
        return "UserCreated";
    }
}
```

**Casos de uso de eventos:**
```
1. Logging/Auditoría
   → Quién hizo qué y cuándo

2. Event Sourcing
   → Reconstruir estado histórico

3. Sincronización
   → Notificar otros servicios

4. Proyecciones
   → Crear vistas de lectura optimizadas
```

---

## 8. Controller: CreateUserController

```java
package cl.drcde.cqrs.presentation.command;

import cl.drcde.cqrs.application.command.CreateUserCommand;
import cl.drcde.cqrs.domain.shared.commandbus.CommandBus;
import cl.drcde.cqrs.domain.shared.commandbus.exception.CommandBusException;
import cl.drcde.cqrs.domain.shared.exception.DomainException;
import cl.drcde.cqrs.presentation.dto.CreateUserDto;
import cl.drcde.cqrs.presentation.shared.ApiResponse;
import cl.drcde.cqrs.presentation.shared.Messages;
import cl.drcde.cqrs.presentation.shared.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller: Punto de entrada HTTP
 * 
 * ✅ VENTAJA: Minimalista
 * ✅ VENTAJA: Solo mapea HTTP → Command
 * ✅ VENTAJA: Manejo centralizado de errores
 * ✅ VENTAJA: Logging de auditoría
 */
@Slf4j
@RestController
@RequestMapping(Routes.User.BASE)
public class CreateUserController {
    
    private final CommandBus commandBus;

    public CreateUserController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    /**
     * Endpoint: POST /users
     * 
     * Request:  { "username": "john", "password": "pass123" }
     * Response: { "status": 201, "data": "Usuario creado", "message": "Created" }
     */
    @PostMapping(value = Routes.User.POST)
    public ResponseEntity<ApiResponse<String>> post(
            @RequestBody CreateUserDto dto
    ) {
        try {
            // 1. Crear comando (data transfer)
            CreateUserCommand command = new CreateUserCommand(
                    dto.getUsername(),
                    dto.getPassword()
            );

            // 2. Ejecutar comando
            this.commandBus.handle(command);

            // 3. Respuesta exitosa
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    Messages.CREATED
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DomainException e) {
            // Error de dominio: validación fallida
            log.warn("Domain error: {}", e.getMessage());
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    Messages.BAD_REQUEST
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (CommandBusException e) {
            // Error de comando
            log.error("Command bus error", e);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Messages.INTERNAL_SERVER_ERROR
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);

        } catch (Exception e) {
            // Error inesperado
            log.error("Unexpected error", e);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Messages.INTERNAL_SERVER_ERROR
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
```

**Responsabilidades del Controller:**
```
✅ Recibir HTTP
✅ Mapear DTO → Command
✅ Manejo de errores
✅ Logging/Auditoría
✅ Serializar respuesta

❌ NO: Lógica de negocio
❌ NO: Acceso a BD
❌ NO: Validaciones complejas
```

---

## 9. Repository Pattern: UserRepository

```java
// INTERFAZ (en domain)
package cl.drcde.cqrs.domain.repository;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.vo.UUIDv4;

public interface UserRepository {
    User save(User user);
    User findById(UUIDv4 id);
    void delete(User user);
    // Puedes agregar más métodos según necesites
}

// IMPLEMENTACIÓN (en infrastructure)
package cl.drcde.cqrs.infrastructure.persistence;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.model.UserId;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ✅ VENTAJA: Domain no conoce que existe JPA
 * ✅ VENTAJA: Puedo cambiar a Mongo, Redis, etc
 * ✅ VENTAJA: Testing sin BD
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        // Mapear Domain → Entity JPA
        UUIDv4 userId = UserId.generate();
        JpaUser jpaUser = new JpaUser(
                userId,
                user.getUsername(),
                user.getPassword()
        );
        
        // Persistir
        JpaUser savedUser = this.jpaUserRepository.save(jpaUser);
        
        // Mapear Entity JPA → Domain
        return new User(
                new UserId(savedUser.getId()),
                savedUser.getUsername(),
                savedUser.getPassword()
        );
    }

    @Override
    public User findById(UUIDv4 id) {
        Optional<JpaUser> jpaUser = this.jpaUserRepository.findById(id.value());
        
        if (jpaUser.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        
        return new User(
                new UserId(jpaUser.get().getId()),
                jpaUser.get().getUsername(),
                jpaUser.get().getPassword()
        );
    }

    @Override
    public void delete(User user) {
        JpaUser jpaUser = new JpaUser(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
        this.jpaUserRepository.delete(jpaUser);
    }
}
```

**Ventaja de esta separación:**
```
Dominio (User)           ≠    BD (JpaUser)
├─ Value Objects             ├─ Entity
├─ Lógica pura               ├─ Anotaciones JPA
├─ Sin BD                    ├─ Converters
└─ Reutilizable             └─ Mappeos

Beneficio:
❌ Dominio nunca cambía por cambios de BD
❌ Puedo tener múltiples implementaciones
```

---

## 10. Unit Test: CreateUserCommandHandlerTest

```java
package cl.drcde.cqrs.application.command;

import cl.drcde.cqrs.domain.model.User;
import cl.drcde.cqrs.domain.repository.UserRepository;
import cl.drcde.cqrs.domain.shared.eventbus.EventBus;
import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * ✅ VENTAJA: SIN BD real
 * ✅ VENTAJA: Rápido (ms, no segundos)
 * ✅ VENTAJA: Determinista
 * ✅ VENTAJA: Independiente
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateUserCommandHandlerTest {

    private static final String USERNAME = "john_doe";
    private static final String PASSWORD = "SecurePass123!";

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private CreateUserCommandHandler handler;

    @Test
    public void testCreateUserCommand_Success() throws Exception {
        // ARRANGE
        CreateUserCommand command = new CreateUserCommand(USERNAME, PASSWORD);

        // ACT
        handler.handle(command);

        // ASSERT
        // Verificar que se persistió
        verify(userRepository, times(1)).save(any(User.class));
        
        // Verificar que se publicaron eventos
        verify(eventBus, times(1)).publish(any());
    }

    @Test(expected = Exception.class)
    public void testCreateUserCommand_InvalidUsername() throws Exception {
        // ARRANGE: Username vacío
        CreateUserCommand command = new CreateUserCommand("", PASSWORD);

        // ACT & ASSERT
        handler.handle(command);
        // Debe lanzar excepción
    }

    @Test(expected = Exception.class)
    public void testCreateUserCommand_InvalidPassword() throws Exception {
        // ARRANGE: Password muy corto
        CreateUserCommand command = new CreateUserCommand(USERNAME, "123");

        // ACT & ASSERT
        handler.handle(command);
        // Debe lanzar excepción
    }
}
```

**Comparativa Testing:**
```
CON Arquitectura Tradicional:
- Tests lentos (necesitan BD)
- Flacos (estado compartido)
- Difíciles de escribir

CON Clean Architecture:
- Tests rápidos (mocks)
- Sólidos (aislados)
- Fáciles de escribir
```

---

## Resumen: Flujo Completo

```
Usuario hace POST /users { username, password }
                    ↓
        CreateUserController.post(DTO)
                    ↓
        CreateUserCommand(username, password)
                    ↓
        CommandBus.handle(command)
                    ↓
        CreateUserCommandHandler.execute()
                    ↓
        Username vo = new Username(username)  ← Validado
        Password vo = new Password(password)  ← Validado
        UserId id = UserId.generate()         ← UUID único
                    ↓
        User user = new User(id, username, password)
                    ↓
        user.getEvents() → CreatedUserEvent  ← Generado automáticamente
                    ↓
        userRepository.save(user)
                    ↓
        JpaUser jpaUser = ... → Persistir en BD
                    ↓
        eventBus.publish(CreatedUserEvent)
                    ↓
        CreatedUserEventBusListener.handle(event) ← Escucha el evento
                    ↓
        Response: 201 Created
```

Cada capa conoce solo la siguiente y la anterior. 🎯

