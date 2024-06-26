# Prueba de concepto para microservicio con prácticas robustas de desarrollo
# Introducción
Este documento proporciona una guía para comprender y utilizar el Microservicio de Ejemplo. El microservicio está desarrollado en Java 11 utilizando Spring Boot y sigue las prácticas de Arquitectura Hexagonal, TDD y CQRS. El objetivo de este microservicio es demostrar la implementación de estas prácticas y tecnologías clave.

![image](https://github.com/eduardo732/cleanArquitecture/assets/54371860/00fe687b-beae-42be-b13c-f73a85d2ce12)


# Características Principales
* Arquitectura Hexagonal: El microservicio sigue el enfoque de Arquitectura Hexagonal (también conocida como Puertos y Adaptadores) para lograr una alta separación de preocupaciones y modularidad.
* TDD (Desarrollo Dirigido por Pruebas): Se ha aplicado TDD durante el desarrollo del microservicio para garantizar la calidad del código y permitir una mayor confianza al realizar cambios o mejoras en el futuro.
* CQRS (Command Query Responsibility Segregation): El microservicio utiliza el patrón CQRS para separar las operaciones de escritura (comandos) y lectura (consultas), lo que permite una mayor escalabilidad y flexibilidad.
* Base de datos MySQL: El microservicio se conecta a una base de datos MySQL para almacenar y recuperar los datos. Se utiliza el mapeo objeto-relacional (ORM) proporcionado por Spring Boot para facilitar las operaciones de persistencia.
* Entidad User: El microservicio maneja la entidad User como ejemplo, pero se puede ampliar fácilmente para incluir otras entidades según las necesidades del negocio.
* Value Objects: Se han aplicado Value Objects en las entidades de dominio para garantizar el cumplimiento de las reglas de negocio y mejorar la encapsulación y reusabilidad del código.
* Identificadores UUID: Los identificadores de las entidades se implementan utilizando el tipo UUID para garantizar la unicidad y evitar conflictos de concurrencia.
* Event Sourcing: Se ha implementado el patrón Event Sourcing para capturar eventos dentro del servicio y permitir el seguimiento y recuperación de cambios en los estados de las entidades.

# Requisitos Previos
Antes de comenzar a utilizar el Microservicio de Ejemplo, asegúrese de tener instalado lo siguiente:

Java 11 (JDK)
MySQL Server
IDE de su elección (recomendado: IntelliJ IDEA, Eclipse)
Instalación y Configuración
Siga los pasos a continuación para instalar y configurar el Microservicio de Ejemplo:

Clone el repositorio del microservicio desde GitHub: [enlace_al_repositorio].
Importe el proyecto en su IDE.
Configure la conexión a la base de datos MySQL en el archivo de configuración application.properties o application.yml. Asegúrese de proporcionar el nombre de la base de datos, el nombre de usuario y la contraseña adecuados.
Ejecute los scripts de inicialización de la base de datos para crear las tablas y cargar los datos iniciales si es necesario.
Compile y ejecute el microservicio desde su IDE o utilizando los comandos de construcción proporcionados (por ejemplo, ./mvnw spring-boot:run).

# Uso del Microservicio
Una vez que el Microservicio de Ejemplo esté en funcionamiento, puede realizar las siguientes operaciones:

Crear un usuario: Envíe una solicitud POST al endpoint /users con los datos del usuario en el cuerpo de la solicitud. El microservicio validará los datos y creará un nuevo usuario en la base de datos.
Obtener un usuario: Envíe una solicitud GET al endpoint /users/{id} para recuperar los detalles de un usuario específico. Proporcione el ID del usuario en la URL.
Actualizar un usuario: Envíe una solicitud PUT al endpoint /users/{id} con los datos actualizados del usuario en el cuerpo de la solicitud. Proporcione el ID del usuario en la URL.
Eliminar un usuario: Envíe una solicitud DELETE al endpoint /users/{id} para eliminar un usuario específico. Proporcione el ID del usuario en la URL.
# Testing
El Microservicio de Ejemplo incluye pruebas unitarias y de integración para garantizar la calidad y el correcto funcionamiento del código. Puede ejecutar las pruebas utilizando su IDE o mediante el comando ./mvnw test en la línea de comandos.
Las pruebas utilizan JUnit 4 y Mockito para simular dependencias y verificar el comportamiento esperado del código.
