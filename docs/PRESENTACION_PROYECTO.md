# Sistema Gestor de Proyectos - Backend Spring Boot
## Documento de Presentación del Proyecto

---

**Centro de Gestión de Mercados, Logística y Tecnologías de la Información**  
**Teleinformática - Análisis y Diseño de Software**  
**SENA**

**Instructor**: Jesús Ropero Barbosa  
**Email**: jropero@sena.edu.co

---

## Índice

1. [¿Qué hace el proyecto?](#1-qué-hace-el-proyecto)
   - 1.1. Gestión de Usuarios
   - 1.2. Gestión de Proyectos
   - 1.3. Registro de Trazabilidad
2. [Arquitectura del Código (Entidades/Modelos)](#2-arquitectura-del-código-entidadesmodelos)
3. [Repositorios (DAOs)](#3-repositorios-daos)
4. [Controladores](#4-controladores)
5. [Configuración y Propiedades](#5-configuración-y-propiedades)
6. [Conclusión](#6-conclusión)
7. [Entrega de URL - Repositorio GitHub](#7-entrega-de-url---repositorio-github)

---

## 1. ¿Qué hace el proyecto?

El **Sistema Gestor de Proyectos** es una aplicación backend desarrollada con **Spring Boot** que permite la administración centralizada de proyectos, usuarios y el registro completo de la trazabilidad de cambios de estado. El sistema está diseñado para instituciones que requieren un control eficiente del ciclo de vida de sus proyectos, desde su radicación hasta su finalización.

### 1.1. Gestión de Usuarios

El sistema permite administrar usuarios con tres roles diferenciados:

- **ADMIN**: Usuarios con permisos administrativos completos. Pueden crear proyectos, registrar usuarios y asignar gestores responsables.
- **GESTOR**: Usuarios responsables de la gestión de proyectos. Pueden actualizar el estado de los proyectos a su cargo y registrar observaciones mediante trazabilidad.
- **INVITADO**: Usuarios con permisos de solo lectura. Únicamente pueden consultar información sin posibilidad de modificar datos.

**Funcionalidades implementadas:**
- ✅ Crear nuevos usuarios con validación de campos obligatorios
- ✅ Listar todos los usuarios registrados en el sistema
- ✅ Consultar información detallada de un usuario por su ID
- ✅ Validación de roles permitidos (ADMIN, GESTOR, INVITADO)
- ✅ Validación de correo único en la base de datos

### 1.2. Gestión de Proyectos

El sistema permite la administración completa del ciclo de vida de los proyectos:

**Estados del Proyecto:**
- **RADICADO**: Estado inicial cuando se crea un proyecto
- **EN_PROCESO**: Proyecto en ejecución activa
- **FINALIZADO**: Proyecto completado exitosamente
- **RECHAZADO**: Proyecto rechazado o cancelado

**Funcionalidades implementadas:**
- ✅ Crear nuevos proyectos con asignación de gestor responsable
- ✅ Listar todos los proyectos con su información completa
- ✅ Consultar detalles específicos de un proyecto por su ID
- ✅ Validación automática de existencia del gestor asignado
- ✅ Generación automática de fechas (radicación y última actualización)
- ✅ Estado inicial automático (RADICADO) al crear un proyecto

### 1.3. Registro de Trazabilidad

La trazabilidad es el componente central que garantiza la auditoría completa del sistema. Cada cambio de estado de un proyecto genera automáticamente un registro histórico que incluye:

**Información registrada:**
- Fecha y hora exacta del cambio
- Usuario que realizó el cambio
- Nuevo estado aplicado al proyecto
- Observación o comentario sobre el cambio

**Funcionalidades implementadas:**
- ✅ Registrar cambios de estado con observaciones detalladas
- ✅ Actualización automática del estado del proyecto
- ✅ Actualización automática de la fecha de última modificación
- ✅ Consultar historial completo de cambios de un proyecto
- ✅ Validación de existencia de proyecto y usuario antes de registrar
- ✅ Transacciones para garantizar integridad de datos

**Reglas de negocio:**
- Al registrar una trazabilidad, el sistema actualiza automáticamente el estado actual del proyecto
- La fecha de última actualización se actualiza automáticamente
- Todos los cambios quedan registrados de forma permanente e inmodificable

---

## 2. Arquitectura del Código (Entidades/Modelos)

El sistema implementa una **arquitectura por capas simple y profesional**, siguiendo el patrón DAO (Data Access Object) para mantener una separación clara de responsabilidades.

### 2.1. Estructura de Capas

```
src/main/java/com/gestorproyectos/
├── controller/     # Capa de presentación (exposición de APIs REST)
├── dao/           # Capa de acceso a datos (operaciones CRUD)
└── model/         # Capa de dominio (entidades del negocio)
```

### 2.2. Entidades del Dominio

#### 2.2.1. Usuario

**Ubicación**: `com.gestorproyectos.model.Usuario`

**Atributos:**
- `idUsuario` (Integer): Identificador único del usuario
- `nombre` (String): Nombre del usuario
- `apellidos` (String): Apellidos del usuario
- `correo` (String): Correo electrónico (único en el sistema)
- `telefono` (String): Número de teléfono de contacto
- `rol` (RolUsuario): Rol del usuario en el sistema

**Características:**
- Implementa constructores vacío y completo
- Métodos getters y setters para todos los atributos
- Método `toString()` para representación en cadena

#### 2.2.2. Proyecto

**Ubicación**: `com.gestorproyectos.model.Proyecto`

**Atributos:**
- `idProyecto` (Integer): Identificador único del proyecto
- `fechaRadicacion` (LocalDateTime): Fecha en que se radicó el proyecto
- `nombre` (String): Nombre del proyecto
- `descripcion` (String): Descripción detallada del proyecto
- `estadoActual` (EstadoProyecto): Estado actual del proyecto
- `fechaUltimaActualizacion` (LocalDateTime): Fecha de la última actualización
- `idGestorAsignado` (Integer): Referencia al usuario gestor asignado (FK)

**Características:**
- Utiliza `LocalDateTime` para manejo de fechas
- Relación con Usuario mediante `idGestorAsignado`
- Estado controlado mediante enum `EstadoProyecto`

#### 2.2.3. Trazabilidad

**Ubicación**: `com.gestorproyectos.model.Trazabilidad`

**Atributos:**
- `idTrazabilidad` (Integer): Identificador único del registro
- `idProyecto` (Integer): Referencia al proyecto (FK)
- `observacion` (String): Observación sobre el cambio de estado
- `usuarioQueRealiza` (Integer): Referencia al usuario que realizó el cambio (FK)
- `fecha` (LocalDateTime): Fecha y hora del registro
- `nuevoEstado` (EstadoProyecto): Nuevo estado aplicado al proyecto

**Características:**
- Registro histórico inmutable de cambios
- Relaciones con Proyecto y Usuario mediante claves foráneas
- Fecha generada automáticamente por el sistema

### 2.3. Enumeraciones

#### 2.3.1. EstadoProyecto

**Ubicación**: `com.gestorproyectos.model.EstadoProyecto`

**Valores:**
- `RADICADO`: Proyecto recién creado
- `EN_PROCESO`: Proyecto en ejecución
- `FINALIZADO`: Proyecto completado
- `RECHAZADO`: Proyecto rechazado o cancelado

#### 2.3.2. RolUsuario

**Ubicación**: `com.gestorproyectos.model.RolUsuario`

**Valores:**
- `ADMIN`: Administrador del sistema
- `GESTOR`: Gestor de proyectos
- `INVITADO`: Usuario con permisos de solo lectura

### 2.4. Justificación de la Arquitectura

La arquitectura por capas implementada ofrece las siguientes ventajas:

- **Separación de Responsabilidades**: Cada capa tiene una función específica y bien definida
- **Mantenibilidad**: El código es fácil de entender y modificar
- **Escalabilidad**: Permite agregar nuevas funcionalidades sin afectar otras capas
- **Testabilidad**: Cada capa puede ser probada de forma independiente
- **Simplicidad**: Evita complejidad innecesaria, ideal para proyectos formativos

---

## 3. Repositorios (DAOs)

Los **Data Access Objects (DAO)** son los componentes responsables de la comunicación con la base de datos MySQL. Utilizan **Spring JDBC** y **JdbcTemplate** para ejecutar consultas SQL de forma segura y eficiente.

### 3.1. UsuarioDAO

**Ubicación**: `com.gestorproyectos.dao.UsuarioDAO`

**Responsabilidades:**
- Gestionar todas las operaciones CRUD sobre la tabla `usuario`
- Validar existencia de usuarios
- Mapear resultados de consultas SQL a objetos Java

**Métodos implementados:**

| Método | Descripción | SQL |
|--------|-------------|-----|
| `crear(Usuario)` | Inserta un nuevo usuario en la base de datos | `INSERT INTO usuario` |
| `listar()` | Obtiene todos los usuarios ordenados por ID | `SELECT * FROM usuario ORDER BY id_usuario` |
| `buscarPorId(Integer)` | Busca un usuario específico por su ID | `SELECT * FROM usuario WHERE id_usuario = ?` |
| `existe(Integer)` | Verifica si un usuario existe en la base de datos | `SELECT COUNT(*) FROM usuario WHERE id_usuario = ?` |

**Características técnicas:**
- Utiliza `RowMapper` para mapear `ResultSet` a objetos `Usuario`
- Manejo de excepciones SQL
- Validación de existencia antes de operaciones críticas

### 3.2. ProyectoDAO

**Ubicación**: `com.gestorproyectos.model.ProyectoDAO`

**Responsabilidades:**
- Gestionar operaciones CRUD sobre la tabla `proyecto`
- Validar existencia del gestor asignado antes de crear proyectos
- Actualizar estado y fechas de proyectos

**Métodos implementados:**

| Método | Descripción | SQL |
|--------|-------------|-----|
| `crear(Proyecto)` | Crea un nuevo proyecto con validaciones | `INSERT INTO proyecto` |
| `listar()` | Lista todos los proyectos | `SELECT * FROM proyecto ORDER BY id_proyecto` |
| `buscarPorId(Integer)` | Busca un proyecto por su ID | `SELECT * FROM proyecto WHERE id_proyecto = ?` |
| `actualizarEstado(Integer, EstadoProyecto, LocalDateTime)` | Actualiza estado y fecha de actualización | `UPDATE proyecto SET estado_actual = ?, fecha_ultima_actualizacion = ?` |
| `existe(Integer)` | Verifica existencia de un proyecto | `SELECT COUNT(*) FROM proyecto WHERE id_proyecto = ?` |

**Reglas de negocio implementadas:**
- ✅ Validación de existencia del gestor antes de crear proyecto
- ✅ Asignación automática de estado inicial (RADICADO)
- ✅ Generación automática de fechas de radicación y actualización
- ✅ Conversión de `LocalDateTime` a `Timestamp` para MySQL

### 3.3. TrazabilidadDAO

**Ubicación**: `com.gestorproyectos.dao.TrazabilidadDAO`

**Responsabilidades:**
- Registrar cambios de estado (trazabilidad)
- Consultar historial de cambios por proyecto
- Coordinar actualización automática del proyecto

**Métodos implementados:**

| Método | Descripción | SQL |
|--------|-------------|-----|
| `registrar(Trazabilidad)` | Registra un cambio de estado y actualiza el proyecto | `INSERT INTO trazabilidad` + `UPDATE proyecto` |
| `consultarPorProyecto(Integer)` | Obtiene el historial completo de un proyecto | `SELECT * FROM trazabilidad WHERE id_proyecto = ? ORDER BY fecha ASC` |
| `buscarPorId(Integer)` | Busca una trazabilidad por su ID | `SELECT * FROM trazabilidad WHERE id_trazabilidad = ?` |

**Características técnicas:**
- Utiliza `@Transactional` para garantizar atomicidad
- Ejecuta dos operaciones SQL en una sola transacción:
  1. Insertar registro de trazabilidad
  2. Actualizar estado del proyecto
- Validación de existencia de proyecto antes de registrar
- Generación automática de fecha del sistema

**Flujo de operación:**
```
1. Validar que el proyecto existe
2. Validar que el usuario existe
3. Establecer fecha actual
4. INICIAR TRANSACCIÓN
5. Insertar registro en trazabilidad
6. Actualizar estado del proyecto
7. CONFIRMAR TRANSACCIÓN
```

---

## 4. Controladores

Los **Controladores REST** son la capa de presentación que expone los endpoints HTTP del sistema. Utilizan anotaciones de Spring Boot para mapear rutas y métodos HTTP.

### 4.1. UsuarioController

**Ubicación**: `com.gestorproyectos.controller.UsuarioController`

**Ruta base**: `/api/usuarios`

**Endpoints implementados:**

| Método HTTP | Ruta | Descripción | Código de Respuesta |
|-------------|------|-------------|---------------------|
| `GET` | `/api/usuarios` | Lista todos los usuarios | 200 OK |
| `POST` | `/api/usuarios` | Crea un nuevo usuario | 201 Created / 400 Bad Request |
| `GET` | `/api/usuarios/{id}` | Obtiene un usuario por ID | 200 OK / 404 Not Found |

**Validaciones implementadas:**
- ✅ Nombre obligatorio
- ✅ Apellidos obligatorios
- ✅ Correo obligatorio y único
- ✅ Teléfono obligatorio
- ✅ Rol obligatorio y válido (ADMIN, GESTOR, INVITADO)

**Características:**
- Anotación `@CrossOrigin(origins = "*")` para permitir peticiones desde cualquier origen
- Manejo de excepciones con códigos HTTP apropiados
- Respuestas JSON automáticas mediante Spring Boot

### 4.2. ProyectoController

**Ubicación**: `com.gestorproyectos.controller.ProyectoController`

**Ruta base**: `/api/proyectos`

**Endpoints implementados:**

| Método HTTP | Ruta | Descripción | Código de Respuesta |
|-------------|------|-------------|---------------------|
| `GET` | `/api/proyectos` | Lista todos los proyectos | 200 OK |
| `POST` | `/api/proyectos` | Crea un nuevo proyecto | 201 Created / 400 Bad Request |
| `GET` | `/api/proyectos/{id}` | Obtiene un proyecto por ID | 200 OK / 404 Not Found |

**Validaciones implementadas:**
- ✅ Nombre del proyecto obligatorio
- ✅ Descripción obligatoria
- ✅ Gestor asignado obligatorio
- ✅ Validación de existencia del gestor en la base de datos

**Características:**
- Validación de gestor antes de crear proyecto
- Manejo de errores con mensajes descriptivos
- Respuestas estructuradas en JSON

### 4.3. TrazabilidadController

**Ubicación**: `com.gestorproyectos.controller.TrazabilidadController`

**Ruta base**: `/api/trazabilidad`

**Endpoints implementados:**

| Método HTTP | Ruta | Descripción | Código de Respuesta |
|-------------|------|-------------|---------------------|
| `POST` | `/api/trazabilidad` | Registra un cambio de estado | 201 Created / 400 Bad Request |
| `GET` | `/api/trazabilidad/proyecto/{id}` | Consulta historial de un proyecto | 200 OK |

**Validaciones implementadas:**
- ✅ ID del proyecto obligatorio
- ✅ Observación obligatoria
- ✅ Usuario que realiza la acción obligatorio
- ✅ Nuevo estado obligatorio y válido
- ✅ Validación de existencia de proyecto
- ✅ Validación de existencia de usuario

**Características:**
- Coordinación con `ProyectoDAO` para actualización automática
- Transacciones para garantizar integridad
- Historial ordenado cronológicamente

---

## 5. Configuración y Propiedades

### 5.1. Archivo de Configuración Principal

**Ubicación**: `src/main/resources/application.properties`

Este archivo contiene toda la configuración necesaria para que la aplicación Spring Boot funcione correctamente.

#### 5.1.1. Configuración de Base de Datos

```properties
# Configuración de la base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/gestor_proyectos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=gestor
spring.datasource.password=gestor123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Explicación:**
- **URL**: Define la conexión a MySQL en localhost, puerto 3306, base de datos `gestor_proyectos`
- **Parámetros de conexión**:
  - `useSSL=false`: Desactiva SSL para desarrollo local
  - `serverTimezone=UTC`: Establece zona horaria UTC para evitar problemas con fechas
  - `allowPublicKeyRetrieval=true`: Permite recuperación de clave pública (necesario en algunas configuraciones)
- **Usuario y contraseña**: Credenciales de acceso a MySQL
- **Driver**: Especifica el driver JDBC de MySQL 8.x

#### 5.1.2. Configuración del Pool de Conexiones (HikariCP)

```properties
# Configuración de conexión
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

**Explicación:**
- **maximum-pool-size**: Máximo de 10 conexiones simultáneas a la base de datos
- **minimum-idle**: Mínimo de 5 conexiones mantenidas activas
- **connection-timeout**: Tiempo máximo de espera para obtener una conexión (30 segundos)

#### 5.1.3. Configuración de Logging

```properties
# Logging
logging.level.com.gestorproyectos=DEBUG
logging.level.org.springframework.jdbc=DEBUG
```

**Explicación:**
- **Nivel DEBUG**: Permite ver todas las consultas SQL ejecutadas y operaciones del sistema
- Útil para desarrollo y depuración
- Puede reducirse a `INFO` o `WARN` en producción

#### 5.1.4. Configuración del Servidor

```properties
# Puerto del servidor
server.port=8080
```

**Explicación:**
- Define el puerto donde Spring Boot expondrá la aplicación
- Por defecto es 8080, pero puede cambiarse según necesidades

### 5.2. Configuración de Maven (pom.xml)

**Ubicación**: `pom.xml`

**Dependencias principales:**

1. **Spring Boot Starter Web**: Proporciona funcionalidad web y REST
2. **Spring Boot Starter JDBC**: Proporciona acceso a base de datos mediante JDBC
3. **MySQL Connector**: Driver para conectar con MySQL
4. **Spring Boot DevTools**: Herramientas de desarrollo (recarga automática)

**Configuración de Java:**
- Versión: Java 17
- Encoding: UTF-8
- Compilador: Maven Compiler Plugin

### 5.3. Script de Base de Datos

**Ubicación**: `database/schema.sql`

Este script contiene:
- Creación de la base de datos `gestor_proyectos`
- Definición de las tres tablas principales (usuario, proyecto, trazabilidad)
- Establecimiento de relaciones mediante claves foráneas
- Definición de índices para optimización
- Restricciones de integridad referencial

**Características:**
- Uso de `ENUM` para estados y roles
- Claves foráneas con restricciones `ON DELETE RESTRICT` y `ON DELETE CASCADE`
- Índices en campos frecuentemente consultados

---

## 6. Conclusión

### 6.1. Logros del Proyecto

El desarrollo de este **Sistema Gestor de Proyectos** ha permitido aplicar y consolidar conocimientos fundamentales en desarrollo backend con Spring Boot:

✅ **Arquitectura por Capas**: Implementación exitosa de separación de responsabilidades mediante el patrón DAO

✅ **APIs RESTful**: Diseño e implementación de endpoints REST siguiendo buenas prácticas

✅ **Modelado de Datos**: Diseño de base de datos relacional con integridad referencial

✅ **Validaciones de Negocio**: Implementación de reglas de negocio críticas (validación de gestores, estados, etc.)

✅ **Trazabilidad Completa**: Sistema de auditoría que registra todos los cambios de estado

✅ **Manejo de Transacciones**: Uso de transacciones para garantizar integridad de datos

✅ **Documentación Completa**: DER, pseudocódigo y documentación técnica detallada

### 6.2. Tecnologías y Herramientas Dominadas

- **Spring Boot 3.2.0**: Framework principal para desarrollo backend
- **Spring JDBC**: Acceso a datos mediante JDBC Template
- **MySQL 8.0**: Base de datos relacional
- **Maven**: Gestión de dependencias y construcción del proyecto
- **Java 17**: Lenguaje de programación orientado a objetos
- **REST API**: Diseño e implementación de servicios web RESTful
- **Git/GitHub**: Control de versiones y colaboración

### 6.3. Competencias Desarrolladas

Este proyecto ha permitido desarrollar competencias técnicas y metodológicas:

- **Análisis de Requerimientos**: Interpretación de especificaciones funcionales
- **Diseño de Base de Datos**: Modelado entidad-relación y normalización
- **Programación Orientada a Objetos**: Uso de clases, herencia, encapsulamiento
- **Arquitectura de Software**: Diseño de sistemas por capas
- **APIs REST**: Diseño e implementación de servicios web
- **Validación y Seguridad**: Implementación de validaciones de negocio
- **Documentación Técnica**: Elaboración de documentación profesional

### 6.4. Impacto y Aplicabilidad

El sistema desarrollado es **funcional y escalable**, siguiendo estándares de la industria. Puede ser utilizado como base para sistemas más complejos o adaptado a necesidades específicas de organizaciones.

**Casos de uso reales:**
- Gestión de proyectos en instituciones educativas
- Control de proyectos en entidades públicas
- Seguimiento de iniciativas en empresas privadas
- Auditoría y trazabilidad de procesos

### 6.5. Mejoras Futuras

Aunque el sistema cumple con todos los requisitos del taller, existen oportunidades de mejora:

- Implementación de autenticación y autorización (Spring Security)
- Paginación en listados grandes
- Filtros y búsquedas avanzadas
- Exportación de reportes (PDF, Excel)
- Notificaciones por email
- Interfaz gráfica de usuario (frontend)

---

## 7. Entrega de URL - Repositorio GitHub

### 7.1. Repositorio del Proyecto

El código fuente completo del backend está disponible en el siguiente repositorio de GitHub:

**🔗 URL del Repositorio:**
```
https://github.com/juliodparada-bit/gestor-proyectos-backend
```

### 7.2. Contenido del Repositorio

El repositorio contiene:

✅ **Código fuente completo** del backend Spring Boot
✅ **Script SQL** para creación de la base de datos (`database/schema.sql`)
✅ **Documentación técnica**:
   - README.md con instrucciones completas
   - DER (Diagrama Entidad-Relación) en `docs/DER.md`
   - Pseudocódigo y diagramas de flujo en `docs/Pseudocodigo.md`
✅ **Configuración Maven** (pom.xml)
✅ **Maven Wrapper** para fácil ejecución sin instalar Maven
✅ **Archivo de configuración** (application.properties)

### 7.3. Instrucciones para el Instructor

Para clonar y ejecutar el proyecto:

```bash
# 1. Clonar el repositorio
git clone https://github.com/juliodparada-bit/gestor-proyectos-backend.git
cd gestor-proyectos-backend

# 2. Crear la base de datos
mysql -u root -p < database/schema.sql

# 3. Configurar credenciales en application.properties
# Editar: src/main/resources/application.properties

# 4. Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### 7.4. Endpoints Disponibles para Pruebas

Una vez ejecutada la aplicación, se pueden probar los siguientes endpoints:

**Usuarios:**
- `GET http://localhost:8080/api/usuarios`
- `POST http://localhost:8080/api/usuarios`
- `GET http://localhost:8080/api/usuarios/{id}`

**Proyectos:**
- `GET http://localhost:8080/api/proyectos`
- `POST http://localhost:8080/api/proyectos`
- `GET http://localhost:8080/api/proyectos/{id}`

**Trazabilidad:**
- `POST http://localhost:8080/api/trazabilidad`
- `GET http://localhost:8080/api/trazabilidad/proyecto/{id}`

### 7.5. Nota Importante

El repositorio contiene **únicamente el código del backend**. El frontend desarrollado se mantiene de forma privada y no está incluido en este repositorio público, tal como se solicitó en los requisitos del taller.

---

## Anexos

### A. Estructura Completa del Proyecto

```
gestor-proyectos-backend/
├── .gitignore
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── database/
│   └── schema.sql
├── docs/
│   ├── DER.md
│   ├── Pseudocodigo.md
│   └── PRESENTACION_PROYECTO.md
├── mvnw
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── gestorproyectos/
        │           ├── GestorProyectosApplication.java
        │           ├── controller/
        │           │   ├── ProyectoController.java
        │           │   ├── TrazabilidadController.java
        │           │   └── UsuarioController.java
        │           ├── dao/
        │           │   ├── ProyectoDAO.java
        │           │   ├── TrazabilidadDAO.java
        │           │   └── UsuarioDAO.java
        │           └── model/
        │               ├── EstadoProyecto.java
        │               ├── Proyecto.java
        │               ├── RolUsuario.java
        │               ├── Trazabilidad.java
        │               └── Usuario.java
        └── resources/
            └── application.properties
```

### B. Diagrama de Relaciones

```
Usuario (1) ──── (N) Proyecto
    │                    │
    │                    │
    │                    │ (1)
    │                    │
    │                    │ (N)
    │                    │
    │              Trazabilidad
    │                    │
    │                    │ (N)
    │                    │
    └────────── (1) ──────┘
```

---

**Fin del Documento de Presentación**

---

*Documento generado para el Taller Entrega Final - Desarrollo Backend con Spring Boot*  
*SENA - Centro de Gestión de Mercados, Logística y Tecnologías de la Información*  
*Teleinformática - Análisis y Diseño de Software*

