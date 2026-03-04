# Clean Architecture + CQRS: Microservicio de Ejemplo

**Demostración de arquitectura empresarial escalable con Java 21, Spring Boot 3 y H2**

## 🎯 Introducción

Este proyecto es una **prueba de concepto** que demuestra cómo combinar dos patrones arquitectónicos avanzados:

1. **Clean Architecture (Arquitectura Hexagonal)**: Separación clara de responsabilidades en capas independientes
2. **CQRS (Command Query Responsibility Segregation)**: Segregación de operaciones de lectura y escritura

El microservicio está construido con:
- **Java 21**: Última versión estable con características modernas
- **Spring Boot 3.3.0**: Framework que soporta Jakarta EE
- **H2 Database**: Base de datos en memoria para desarrollo y testing
- **TDD**: Desarrollo dirigido por pruebas

> ⚠️ **Nota**: Esta es una arquitectura compleja. Se recomienda usarla solo cuando los beneficios justifiquen la complejidad adicional.

---

## 🏗️ Arquitectura

### Estructura Conceptual

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION (API REST)                   │
│         Controllers, DTOs, Response Objects                  │
└──────────────────┬──────────────────────────────────────────┘
                   │
┌──────────────────┴──────────────────────────────────────────┐
│              APPLICATION (Orquestación)                       │
│  ┌──────────────────┐          ┌──────────────────┐          │
│  │   Commands       │          │    Queries       │          │
│  ├──────────────────┤          ├──────────────────┤          │
│  │ - CreateUser     │          │ - FindAllUsers   │          │
│  │ - Handlers       │          │ - Handlers       │          │
│  │ - Validators     │          │ - Retrievers     │          │
│  └──────────────────┘          └──────────────────┘          │
│         │                              │                      │
│         └──────────────┬───────────────┘                      │
└──────────────────┬──────────────────────────────────────────┘
                   │
┌──────────────────┴──────────────────────────────────────────┐
│              DOMAIN (Lógica de Negocio)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │   Models     │  │   Events     │  │ Repositories │       │
│  │  (User,      │  │  (CreatedU-  │  │  (Interfaces)│       │
│  │   ValueObj)  │  │   serEvent)  │  │              │       │
│  └──────────────┘  └──────────────┘  └──────────────┘       │
└──────────────────┬──────────────────────────────────────────┘
                   │
┌──────────────────┴──────────────────────────────────────────┐
│         INFRASTRUCTURE (Implementación Técnica)              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │ Persistence  │  │   Event Bus  │  │   Config     │       │
│  │  (JPA, H2)   │  │  (Listeners) │  │ (Beans, DI)  │       │
│  └──────────────┘  └──────────────┘  └──────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

### Flujo de una Operación (Ejemplo: Crear Usuario)

```
1. POST /users → CreateUserController
                      ↓
2. Validar DTO → CreateUserCommand
                      ↓
3. CommandBus.handle() → CreateUserCommandHandler
                      ↓
4. Domain Logic → User.create()
                      ↓
5. Generar Events → CreatedUserEvent
                      ↓
6. Persistir → UserRepository (JpaUserRepository)
                      ↓
7. Publicar Events → EventBus
                      ↓
8. Listeners → CreatedUserEventBusListener
                      ↓
9. Response → ApiResponse<String>
```

---

## 📁 Estructura del Proyecto

```
cqrs/
├── src/main/java/cl/drcde/cqrs/
│   ├── CqrsApplication.java                    # Spring Boot Main
│   │
│   ├── presentation/                           # 📍 CAPA DE PRESENTACIÓN
│   │   ├── command/
│   │   │   └── CreateUserController.java       # Endpoints REST
│   │   ├── query/
│   │   │   └── FindAllUsersController.java
│   │   ├── dto/
│   │   │   └── CreateUserDto.java              # Request DTOs
│   │   └── shared/
│   │       ├── ApiResponse.java                # Respuesta genérica
│   │       ├── Messages.java
│   │       └── Routes.java
│   │
│   ├── application/                            # 📍 CAPA DE APLICACIÓN
│   │   ├── command/
│   │   │   ├── CreateUserCommand.java          # Command CQRS
│   │   │   └── CreateUserCommandHandler.java   # Lógica de comando
│   │   ├── query/
│   │   │   ├── FindAllUsersQuery.java          # Query CQRS
│   │   │   └── FindAllUsersQueryHandler.java   # Lógica de consulta
│   │   └── listener/
│   │       └── CreatedUserEventBusListener.java
│   │
│   ├── domain/                                 # 📍 CAPA DE DOMINIO
│   │   ├── model/
│   │   │   ├── User.java                       # Aggregate Root
│   │   │   ├── UserId.java                     # ID específico de dominio
│   │   │   └── UserService.java                # Lógica de negocio
│   │   ├── repository/
│   │   │   └── UserRepository.java             # Interfaz del repositorio
│   │   ├── events/
│   │   │   └── CreatedUserEvent.java           # Event Sourcing
│   │   ├── vo/ (Value Objects)
│   │   │   ├── Active.java
│   │   │   ├── Password.java
│   │   │   ├── Username.java
│   │   │   ├── UUIDv4.java
│   │   │   └── ValueObject.java
│   │   └── shared/
│   │       ├── aggregateroot/
│   │       ├── commandbus/
│   │       ├── eventbus/
│   │       ├── querybus/
│   │       └── exception/
│   │
│   └── infrastructure/                         # 📍 CAPA DE INFRAESTRUCTURA
│       ├── config/
│       │   ├── CommandConfig.java              # Registro de handlers
│       │   └── QueryConfig.java
│       ├── persistence/
│       │   ├── JpaUser.java                    # Entity JPA
│       │   ├── JpaUserRepository.java          # Spring Data Repository
│       │   ├── UserRepositoryImpl.java          # Implementación
│       │   ├── model/
│       │   │   └── JpaBaseModel.java           # Base con BD
│       │   └── converter/
│       │       ├── ActiveAttributeConverter.java
│       │       ├── PasswordAttributeConverter.java
│       │       ├── UsernameAttributeConverter.java
│       │       └── UUIDv4AttributeConverter.java
│       ├── eventbus/
│       │   ├── SpringEventBus.java
│       │   ├── SpringCommandBus.java
│       │   ├── SpringQueryBus.java
│       │   └── ConsoleEventBus.java
│       └── config/
│           └── application.properties
│
└── src/test/java/cl/drcde/cqrs/               # Tests unitarios
    ├── presentation/
    ├── application/
    ├── domain/
    └── infrastructure/
```

---

## ⚖️ Ventajas y Desventajas

### ✅ Ventajas

#### 1. **Escalabilidad Extrema**
- Las operaciones de lectura (queries) y escritura (commands) están completamente separadas
- Puedes escalar independientemente: más réplicas de BD para lecturas, escritura centralizada
- Bases de datos diferentes para lectura y escritura (CQRS Database Separation)

```java
// ✨ Puedes optimizar independientemente
Queries → Elasticsearch, Redis (Rápido)
Commands → PostgreSQL (Consistente)
```

#### 2. **Independencia de Cambios de BD**
- El dominio no está acoplado a la BD
- Cambiar de H2 a PostgreSQL solo afecta `infrastructure/`
- Puedes tener múltiples implementaciones del repositorio

```java
// Mismo interfaz, múltiples implementaciones
UserRepository → JpaUserRepository (SQL)
UserRepository → MongoUserRepository (NoSQL)
UserRepository → RedisUserRepository (Cache)
```

#### 3. **Testing Simplificado**
- Mock de repositorios es fácil
- Lógica de negocio sin dependencias de BD
- Tests unitarios rápidos y confiables

```java
@Mock UserRepository userRepository;
// Mock inyectado automáticamente
```

#### 4. **Mantenibilidad a Largo Plazo**
- Código altamente organizado y predecible
- Nuevos desarrolladores entienden dónde va cada código
- Cambios en un área no afectan a otras

#### 5. **Event Sourcing Integrado**
- Auditoría completa de cambios
- Recuperación de fallos
- Proyecciones múltiples del mismo evento

### ❌ Desventajas

#### 1. **Excesiva Complejidad** ⚠️
- Mucho código boilerplate
- Muchas capas a atravesar para una operación simple
- Curva de aprendizaje pronunciada

```
GET /users → Controller → Handler → Service → Repository → BD
                        ↑
                  Mucho código por una lectura simple
```

#### 2. **Overhead de Rendimiento**
- Más objetos creados (DTO → Command → VO → Entity)
- Mapeos adicionales entre capas
- Overhead de reflexión en frameworks

#### 3. **Overkill para CRUD Simple**
```java
// Para una operación simple, necesitas:
// - Controller
// - DTO
// - Command/Query
// - Handler
// - Service
// - Repository
// - Entity
// - ValueObjects
// - Convertidores
// - Tests para cada capa

// Resultado: 20+ archivos para crear un usuario
```

#### 4. **Consistencia Eventual**
- Con CQRS y event sourcing, no tienes consistencia inmediata
- Los datos de lectura pueden estar desactualizados momentáneamente
- Necesitas UI que maneje esta realidad

#### 5. **Debugging Complejo**
- Flujo de ejecución atraviesa múltiples capas
- Stacktraces largos
- Más puntos de fallo

---

## 🎯 Casos de Uso Ideales

### ✅ CUÁNDO USAR CQRS + Clean Architecture

#### 1. **Sistemas de Alta Escala**
```
Ejemplo: Plataforma de streaming (Netflix, Spotify)
- Millones de lecturas vs pocas escrituras
- Escrituras: catalogar contenido
- Lecturas: recomendaciones, búsqueda, favoritos

Beneficio: Escalar BD de lectura independientemente
```

#### 2. **Aplicaciones con Lógica de Negocio Compleja**
```
Ejemplo: Sistema bancario
- Muchas reglas de validación
- Auditoría crítica
- Eventos importantes (transacciones, fraudes)

Beneficio: Lógica de negocio aislada y testeable
```

#### 3. **Dominios que Evolucionan Rápido**
```
Ejemplo: Startup de e-commerce
- Cambios frecuentes en requisitos
- Múltiples equipos trabajando
- Necesidad de agregar nuevas funcionalidades

Beneficio: Cambios aislados a una capa
```

#### 4. **Sistemas con Múltiples Equipos**
```
Ejemplo: Plataforma grande
- Equipo A: usuarios
- Equipo B: órdenes
- Equipo C: pagos

Beneficio: Equipos independientes por contexto acotado (DDD)
```

#### 5. **Auditoría y Cumplimiento Normativo**
```
Ejemplo: Sistema financiero o sanitario
- GDPR, PCI-DSS, HIPAA
- Necesidad de auditar cada cambio
- Recuperación de estados anteriores

Beneficio: Event Sourcing proporciona auditoría completa
```

### ❌ CUÁNDO NO USAR CQRS + Clean Architecture

#### 1. **CRUD Simple**
```
❌ NO USAR para:
- Blog básico
- Dashboard administrativo simple
- Formularios de contacto

✅ USAR: Spring Data JPA simple + Spring MVC
```

#### 2. **MVP o Prototipo**
```
❌ NO USAR en fase inicial
- Necesitas iterar rápido
- Cambios en el modelo
- Poco claro el dominio

✅ USAR: Arquitectura simple hasta validar mercado
```

#### 3. **Equipo Pequeño o Junior**
```
❌ NO USAR sin experiencia
- Difícil de mantener
- Muchos errores sutiles
- Lentifica development

✅ USAR: Después de 6+ meses de experiencia
```

#### 4. **Baja Carga o Pocas Transacciones**
```
❌ NO USAR si:
- <1000 usuarios
- <100 transacciones/segundo
- No hay separación lectura/escritura

✅ USAR: Arquitectura monolítica simple
```

---

## 📋 Comparativa: Nivel de Complejidad

| Aspecto | CRUD Simple | CQRS + Clean Arch |
|---------|-------------|-------------------|
| **Tiempo Setup** | 1 hora | 1-2 días |
| **Archivos** | 3-5 | 30+ |
| **Curva Aprendizaje** | Baja | Alta |
| **Testing** | Moderado | Completo |
| **Mantenibilidad (6 meses)** | Difícil | Fácil |
| **Escalabilidad** | Limitada | Extrema |
| **Para CRUD** | Ideal | Overkill |

---

## 🔧 Requisitos Previos

- **Java 21** (JDK): [Descargar](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.8+**: Incluido con JDK o [descargar](https://maven.apache.org/)
- **IDE recomendado**: IntelliJ IDEA, VS Code + Extension Pack
- **Git**: Para clonar el repositorio
- **Postman o cURL**: Para probar endpoints

### Verificar Instalación

```bash
# Java
java -version
# Debe mostrar: java version "21.x.x"

# Maven
mvn --version
# Debe mostrar: Apache Maven 3.8+
```

---

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tuusuario/cleanArquitecture.git
cd cleanArquitecture/cqrs
```

### 2. Compilar el Proyecto

```bash
# Limpiar y compilar
./mvnw clean install -DskipTests

# En Windows (sin ./):
mvnw.cmd clean install -DskipTests
```

### 3. Ejecutar la Aplicación

```bash
# Opción 1: Maven
./mvnw spring-boot:run

# Opción 2: Ejecutar JAR
java -jar target/cqrs-0.0.1-SNAPSHOT.jar

# Opción 3: IDE - Click en "Run" o Shift+F10
```

### 4. Verificar que Está Corriendo

```bash
# La aplicación iniciará en http://localhost:8080

# Verificar H2 Console
http://localhost:8080/h2-console

# Credenciales H2:
# URL: jdbc:h2:mem:testdb
# Usuario: sa
# Contraseña: (vacía)
```

---

## 💻 Uso del Microservicio

### Endpoints Disponibles

#### 1. Crear Usuario (POST)

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123!"
  }'
```

**Respuesta exitosa (201)**:
```json
{
  "status": 201,
  "data": "Usuario creado exitosamente",
  "message": "Created"
}
```

#### 2. Obtener Todos los Usuarios (GET)

```bash
curl -X GET http://localhost:8080/users
```

**Respuesta (200)**:
```json
{
  "status": 200,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "username": "john_doe",
      "password": "SecurePass123!",
      "active": true
    }
  ],
  "message": "Success"
}
```

### Flujo de Uso

1. **Crear usuario**: `POST /users`
2. **Ver usuarios**: `GET /users`
3. **Acceder a H2 Console**: `http://localhost:8080/h2-console`
4. **Ejecutar SQL**: 
   ```sql
   SELECT * FROM app_user;
   SELECT * FROM app_user WHERE username = 'john_doe';
   ```

---

## ✅ Testing

### Ejecutar Tests

```bash
# Todos los tests
./mvnw test

# Solo tests de un módulo
./mvnw test -Dtest=UserTest

# Con reporte de cobertura
./mvnw test jacoco:report
```

### Ejemplos de Tests Incluidos

```java
// Domain Model Test
@Test
public void testUserCreation() {
    UserId userId = UserId.generate();
    User user = new User(userId, username, password);
    assertEquals(userId, user.getId());
}

// Handler Test
@Test
public void testCreateUserCommand() throws CommandBusException {
    CreateUserCommand cmd = new CreateUserCommand("john", "pass");
    commandBus.handle(cmd);
    verify(commandBus, times(1)).handle(any());
}

// Controller Test
@Test
public void testCreateUserEndpoint() {
    CreateUserDto dto = new CreateUserDto();
    ResponseEntity<ApiResponse<String>> response = 
        controller.post(dto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
}
```

---

## 🏛️ Explicación de Conceptos Clave

### Clean Architecture / Hexagonal

**Objetivo**: Aislar la lógica de negocio de detalles técnicos

- **Dominio**: Lógica pura, sin dependencias
- **Application**: Orquestación, sin detalles técnicos
- **Infrastructure**: Implementación de interfaces del dominio
- **Presentation**: UI/API, consumidor de la aplicación

### CQRS

**Objetivo**: Separar lecturas de escrituras para escalabilidad

```java
// Escritura (Command)
public class CreateUserCommand {
    // Datos para crear
}

// Lectura (Query)
public class FindAllUsersQuery {
    // Parámetros de búsqueda
}

// ✨ Beneficio: Puedes usar BDs diferentes
```

### Value Objects

**Objetivo**: Encapsular datos con comportamiento

```java
// ❌ Sin Value Object
String username = "john_doe";
if (username.isEmpty()) { } // Validación dispersa

// ✅ Con Value Object
Username username = new Username("john_doe");
// Validación centralizada, reutilizable
```

### Event Sourcing

**Objetivo**: Almacenar eventos en lugar de estados

```java
// ❌ Tradicional: Guardas el estado final
user.active = true; // Solo sabes el estado actual

// ✅ Event Sourcing: Guardas eventos
CreatedUserEvent: Usuario "john" creado
UserActivatedEvent: Usuario "john" activado
// Puedes reconstruir cualquier estado anterior
```

### Aggregate Root

**Objetivo**: Entidad raíz de un contexto acotado

```java
public class User extends BaseModel implements AggregateRoot {
    private Username username;
    private Password password;
    private EventCollection events;
    
    // El User es responsable de su consistencia
    // Los cambios se hacen a través de métodos del User
    // Los eventos se emiten automáticamente
}
```

---

## 📊 Estadísticas del Proyecto

```
Líneas de Código:
- Dominio: ~200 líneas (lógica pura)
- Aplicación: ~150 líneas (orquestación)
- Infraestructura: ~300 líneas (técnico)
- Presentación: ~100 líneas (API)
- Tests: ~500 líneas (cobertura completa)
-------
Total: ~1,250 líneas

Archivos: ~40
Clases: ~30
Tests: ~8-10
Complejidad: Media-Alta
```

---

## 🔗 Referencias

### Libros
- **"Clean Architecture"** - Robert C. Martin (Uncle Bob)
- **"Domain-Driven Design"** - Eric Evans
- **"Building Microservices"** - Sam Newman

### Artículos
- [CQRS Pattern - Microsoft Docs](https://docs.microsoft.com/en-us/azure/architecture/patterns/cqrs)
- [Hexagonal Architecture - Alistair Cockburn](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
- [Event Sourcing - Martin Fowler](https://martinfowler.com/eaaDev/EventSourcing.html)

### Tecnologías Usadas
- [Spring Boot 3.3](https://spring.io/projects/spring-boot)
- [Jakarta EE](https://jakarta.ee/)
- [H2 Database](http://www.h2database.com/)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)

---

## ❓ Preguntas Frecuentes

### ¿Debo usar esta arquitectura para mi proyecto?

**Respuesta**: Solo si tienes:
1. Lógica de negocio compleja ✓
2. Equipo experimentado ✓
3. Necesidad de escalabilidad ✓
4. Contexto acotado bien definido ✓

Si no, comienza con arquitectura simple.

### ¿Cuál es la diferencia con MVC tradicional?

| MVC | CQRS + Clean Arch |
|-----|-------------------|
| Model = BD | Domain ≠ BD |
| Poco testeable | Muy testeable |
| Rápido de empezar | Lento de empezar |
| Difícil de escalar | Fácil de escalar |

### ¿Puedo combinar con microservicios?

**Sí**, es ideal:
```
Microservicio 1 (Users) → Clean Arch + CQRS
Microservicio 2 (Orders) → Clean Arch + CQRS
...
```

Cada servicio es independiente pero sigue el mismo patrón.

---

## 📝 Notas Importantes

### Java 21 vs Versiones Anteriores
```
✨ Java 21 Cambios:
- Jakarta EE (no javax)
- Records (tipos inmutables)
- Virtual Threads (concurrencia)
- Pattern Matching mejorado
```

### H2 vs MySQL
```
Desarrollo/Testing: H2 (en memoria, rápido)
Producción: PostgreSQL, MySQL (persistente)

El código es agnóstico, solo cambia:
- application.properties
- Dependencia de BD en pom.xml
```

---

## 🚧 Mejoras Futuras

- [ ] Implementar API Gateway
- [ ] Agregar autenticación JWT
- [ ] Database Separation (CQRS puro)
- [ ] Elasticsearch para búsquedas
- [ ] Redis para caché
- [ ] Documentación Swagger
- [ ] Actuator metrics
- [ ] Docker + Kubernetes

---

## 📞 Contacto y Contribuciones

Para preguntas o sugerencias sobre este proyecto, puedes:

1. Abrir un issue en GitHub
2. Crear un pull request con mejoras
3. Contactar al autor

---

**Última actualización**: Marzo 2025
**Versión Java**: 21
**Versión Spring Boot**: 3.3.0
**Base de Datos**: H2 (Development) / PostgreSQL (Recommended Production)
