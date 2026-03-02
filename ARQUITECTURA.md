# Guía Visual: CQRS + Clean Architecture

## 1. Comparativa Visual: Arquitectura Tradicional vs CQRS + Clean Arch

### ❌ Arquitectura Tradicional (MVC)

```
                    USUARIO
                      ↓
                    ROUTER
                      ↓
        ┌─────────────────────┐
        │    CONTROLLER       │
        │   (Gestiona todo)   │
        └──────────┬──────────┘
                   ↓
        ┌─────────────────────┐
        │      MODEL          │
        │   (BD + Lógica)     │
        └──────────┬──────────┘
                   ↓
        ┌─────────────────────┐
        │      VIEW           │
        │   (HTML/JSON)       │
        └─────────────────────┘

Problemas:
- ❌ Controller hace todo
- ❌ Modelo acoplado a BD
- ❌ Difícil de testear
- ❌ Cambios esparcidos por todo el código
```

### ✅ Clean Architecture + CQRS

```
                    USUARIO
                      ↓
    ┌─────────────────┴─────────────────┐
    ↓                                   ↓
  CREAR USUARIO                   CONSULTAR USUARIO
    ↓                                   ↓
POST /users                         GET /users
    ↓                                   ↓
┌──────────────────┐          ┌──────────────────┐
│ CreateUserCommand│          │ FindAllUsersQuery│
└────────┬─────────┘          └────────┬─────────┘
         ↓                             ↓
┌──────────────────────┐    ┌──────────────────────┐
│ CommandHandler       │    │ QueryHandler        │
└────────┬─────────────┘    └────────┬─────────────┘
         ↓                           ↓
┌──────────────────────┐    ┌──────────────────────┐
│ Domain Layer         │    │ Application Service  │
│ (User, UserId, etc)  │    │ (FindAllUsers)       │
└────────┬─────────────┘    └────────┬─────────────┘
         ↓                           ↓
┌──────────────────────┐    ┌──────────────────────┐
│ UserRepository       │    │ UserRepository       │
│ (Interfaz)           │    │ (Interfaz)           │
└────────┬─────────────┘    └────────┬─────────────┘
         ↓                           ↓
┌──────────────────────┐    ┌──────────────────────┐
│ JpaUserRepository    │    │ JpaUserRepository    │
│ (H2/MySQL)           │    │ (Redis/Cache)        │
└──────────────────────┘    └──────────────────────┘
```

---

## 2. Flujo Detallado: Crear Usuario

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. REST API REQUEST                                              │
│    POST /users                                                   │
│    { "username": "john", "password": "pass123" }               │
└────────────────────────┬────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│ 2. PRESENTATION LAYER                                            │
│    CreateUserController.post(CreateUserDto)                     │
│    ✅ Validación básica de entrada                             │
│    ✅ Mapeo DTO → Command                                       │
└────────────────────────┬────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│ 3. APPLICATION LAYER                                             │
│    CreateUserCommand {username, password}                       │
│    CommandBus.handle(command)                                   │
│    CreateUserCommandHandler.execute()                           │
│    ✅ Orquestación                                              │
│    ✅ Validaciones de negocio                                   │
└────────────────────────┬────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│ 4. DOMAIN LAYER                                                  │
│    User.create(username, password)                              │
│    ✅ Lógica pura de negocio                                   │
│    ✅ Sin dependencias de BD                                    │
│    ✅ Genera eventos (CreatedUserEvent)                         │
└────────────────────────┬────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│ 5. INFRASTRUCTURE LAYER - PERSISTENCE                            │
│    UserRepositoryImpl.save(user)                                 │
│    JpaUserRepository.save(jpaUser)                              │
│    ✅ Mapeo Domain → Entity JPA                                 │
│    ✅ Persiste en BD (H2/MySQL)                                 │
│    ✅ Retorna ID generado                                       │
└────────────────────────┬────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│ 6. INFRASTRUCTURE LAYER - EVENT PUBLISHING                       │
│    EventBus.publish(CreatedUserEvent)                           │
│    ✅ Notifica listeners                                        │
│    ✅ Auditoría                                                 │
│    ✅ Proyecciones adicionales                                  │
└────────────────────────┬────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│ 7. RESPONSE                                                      │
│    ApiResponse {                                                │
│      status: 201,                                               │
│      data: "Usuario creado exitosamente",                       │
│      message: "Created"                                         │
│    }                                                            │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3. Separación de Responsabilidades por Capa

### 🎨 Presentation Layer
```
✅ QUÉ HACER:
- Validar formato de entrada
- Mapear DTO → Commands/Queries
- Serializar respuestas

❌ NO HACER:
- Lógica de negocio
- Acceso a BD
- Cálculos complejos
```

### 📱 Application Layer
```
✅ QUÉ HACER:
- Orquestar CommandHandlers/QueryHandlers
- Validaciones de negocio
- Coordinar transacciones

❌ NO HACER:
- Acceso directo a BD
- Implementación de reglas complejas
- Detalles técnicos
```

### 💼 Domain Layer
```
✅ QUÉ HACER:
- Lógica pura de negocio
- Generar eventos
- Validaciones de dominio

❌ NO HACER:
- Acceso a BD
- HTTP calls
- Frameworks específicos
```

### ⚙️ Infrastructure Layer
```
✅ QUÉ HACER:
- Implementar interfaces del dominio
- Acceso a BD
- Publicación de eventos
- Configuración de Spring

❌ NO HACER:
- Lógica de negocio
- Validaciones complejas
- Orquestación
```

---

## 4. Value Objects: Encapsulación de Datos

```
❌ ANTES (Sin Value Objects):
class User {
    String username;
    String password;
    
    // Validación dispersa por el código
    if (username.isEmpty()) {}
    if (username.length() > 500) {}
    if (password.length() < 8) {}
    // ... en múltiples lugares
}

✅ DESPUÉS (Con Value Objects):
class Username extends ValueObject {
    private final String value;
    
    public Username(String value) {
        if (value.trim().isEmpty()) 
            throw new DomainException("Username vacío");
        if (value.length() > 500)
            throw new DomainException("Username muy largo");
        this.value = value;
    }
}

class User {
    Username username;  // ✅ Siempre válido
    Password password;  // ✅ Siempre válido
}
```

---

## 5. Event Sourcing: Historia Completa

```
┌─────────────────┐
│ ESTADO INICIAL  │
│ (Sin usuarios)  │
└────────┬────────┘

                    ↓ Evento

        ┌──────────────────────┐
        │ CreatedUserEvent     │
        │ ├─ userId: uuid1     │
        │ ├─ username: john    │
        │ └─ timestamp: t1     │
        └──────────┬───────────┘

                    ↓ Se aplica el evento

        ┌──────────────────────┐
        │ ESTADO 1             │
        │ users = [john]       │
        └──────────┬───────────┘

                    ↓ Evento

        ┌──────────────────────┐
        │ CreatedUserEvent     │
        │ ├─ userId: uuid2     │
        │ ├─ username: jane    │
        │ └─ timestamp: t2     │
        └──────────┬───────────┘

                    ↓ Se aplica el evento

        ┌──────────────────────┐
        │ ESTADO 2             │
        │ users = [john, jane] │
        └──────────┬───────────┘

                    ↓ Evento

        ┌──────────────────────┐
        │ UserActivatedEvent   │
        │ ├─ userId: uuid1     │
        │ └─ timestamp: t3     │
        └──────────┬───────────┘

                    ↓ Se aplica el evento

        ┌──────────────────────┐
        │ ESTADO 3             │
        │ users = [           │
        │   {id: uuid1,       │
        │    active: true},   │
        │   {id: uuid2,       │
        │    active: false}   │
        │ ]                   │
        └──────────────────────┘

BENEFICIOS:
✅ Puedo ver TODA la historia
✅ Puedo volver a cualquier punto en el tiempo
✅ Auditoría completa
✅ Debugging más fácil
```

---

## 6. CQRS: Separación Lectura-Escritura

```
MODELO TRADICIONAL (Lectura + Escritura juntas):
┌──────────────────┐
│ MISMO MODELO     │
├──────────────────┤
│ CREATE USER      │ ← Escribe en BD
│ GET USER         │ ← Lee de BD
│ UPDATE USER      │ ← Escribe en BD
│ DELETE USER      │ ← Escribe en BD
│ SEARCH USERS     │ ← Lee de BD (lento)
│ GET STATS        │ ← Lee de BD (muy lento)
└──────────────────┘

Problema:
- Lectura y escritura compiten por recursos
- Las búsquedas complejas son lentas
- Difícil de escalar


MODELO CQRS (Separación):

ESCRITURA                           LECTURA
┌──────────────────┐             ┌──────────────────┐
│ BD PRIMARIA      │             │ BD DE LECTURA    │
│ (Normalizada)    │             │ (Optimizada)     │
├──────────────────┤             ├──────────────────┤
│ ✅ CREATE        │             │ ✅ GET_USER      │
│ ✅ UPDATE        │             │ ✅ SEARCH_USERS  │
│ ✅ DELETE        │             │ ✅ GET_STATS     │
│                  │ ─ Sync →    │ ✅ GET_REPORTS   │
│ (Consistencia)   │             │ (Rendimiento)    │
└──────────────────┘             └──────────────────┘

Ventajas:
✅ Escalado independiente
✅ Optimización específica
✅ Mejor rendimiento
✅ Historial completo
```

---

## 7. Matriz de Decisión: ¿Usar esta arquitectura?

```
Responde SÍ o NO a cada pregunta:

1. ¿Mi lógica de negocio es compleja? [SÍ/NO]
2. ¿Tengo equipo experimentado en Java/Spring? [SÍ/NO]
3. ¿Necesito escalar a millones de usuarios? [SÍ/NO]
4. ¿Mi dominio está bien definido? [SÍ/NO]
5. ¿Tengo múltiples equipos trabajando? [SÍ/NO]
6. ¿Necesito auditoría completa? [SÍ/NO]

PUNTUACIÓN:
6 SÍ:    ✅✅✅ Excelente - USA esta arquitectura
4-5 SÍ:  ✅✅  Buena opción - Considera usar
2-3 SÍ:  ✅   Posible - Empieza simple y evoluciona
0-1 SÍ:  ❌   No uses - Sobrecomplejo para tu caso
```

---

## 8. Hoja de Ruta: Desde Simple a CQRS

```
FASE 1: Simple (Mes 1)
┌─────────────────────┐
│ Spring Data JPA     │
│ MVC básico          │
└─────────────────────┘

         ↓ (Conforme creces)

FASE 2: Capas (Mes 2-3)
┌─────────────────────┐
│ + Service Layer     │
│ + DTO              │
│ + Repositorios     │
└─────────────────────┘

         ↓ (Lógica más compleja)

FASE 3: Clean Arch (Mes 4-6)
┌─────────────────────┐
│ + Domain Layer      │
│ + Value Objects    │
│ + Interfaces       │
└─────────────────────┘

         ↓ (Necesitas escalabilidad)

FASE 4: CQRS (Mes 7+)
┌─────────────────────┐
│ + Commands/Queries │
│ + Event Sourcing   │
│ + Buses            │
│ + Event Handlers   │
└─────────────────────┘

⏰ NO hagas todo al principio
📈 Evoluciona conforme creces
🎯 Cada arquitectura tiene su momento
```

---

## 9. Ejemplo Real: Netflix

¿Cómo Netflix usa CQRS + Clean Architecture?

```
ESCRITURA:
┌─────────────────┐
│ Cuando Netflix  │
│ agrega una      │
│ nueva serie     │
└────────┬────────┘
         ↓
┌─────────────────┐
│ BD Primaria     │
│ (Postgres)      │
│ - Validación    │
│ - Transacciones │
└────────┬────────┘
         ↓
    Event emitted

LECTURA (Paralelo):
┌─────────────────┐
│ Usuario busca   │
│ "Breaking Bad"  │
└────────┬────────┘
         ↓
┌─────────────────┐
│ Elasticsearch   │
│ (Búsqueda)      │
└────────┬────────┘
         ↓
┌─────────────────┐
│ Redis Cache     │
│ (Recomendaciones)
└────────┬────────┘
         ↓
    Respuesta rápida

RESULTADO:
- ✅ Búsquedas ultra-rápidas
- ✅ Recomendaciones personalizadas
- ✅ Escalable a millones de usuarios
- ✅ Historial completo de cambios
```

---

## 10. Cheat Sheet: Archivos Clave

```
Cuando hagas cambios en:

🔵 DOMINIO (Business Logic)
Archivos: domain/model/*.java, domain/vo/*.java
Afecta a: Todo (más importantes)
Tests: Domain tests
Impacto: ALTO - Cambios en servicios, handlers

🟣 APLICACIÓN (Orquestación)
Archivos: application/command/*.java, application/query/*.java
Afecta a: Handlers, listeners
Tests: Application tests
Impacto: MEDIO - Nuevos comandos, nuevas consultas

🟢 INFRAESTRUCTURA (Técnico)
Archivos: infrastructure/persistence/*.java, infrastructure/eventbus/*
Afecta a: Cómo se persisten/recuperan datos
Tests: Integration tests
Impacto: BAJO - Cambio de BD, event bus

🔴 PRESENTACIÓN (API)
Archivos: presentation/controller/*.java, presentation/dto/*.java
Afecta a: Contratos API
Tests: Controller tests
Impacto: EXTERNO - Clientes de la API
```

---

**Recuerda**: La arquitectura es un medio, no un fin. 
Úsala cuando agregue valor, no por ser "la mejor".

