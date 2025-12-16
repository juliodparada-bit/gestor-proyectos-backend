# Sistema Gestor de Proyectos - Backend Spring Boot

Sistema backend desarrollado con Spring Boot para la gestión de proyectos, usuarios y trazabilidad de cambios de estado.

## 📋 Descripción

Este sistema permite administrar proyectos, usuarios y mantener un historial completo (trazabilidad) de todos los cambios de estado realizados sobre cada proyecto. Implementa una arquitectura por capas simple y profesional utilizando el patrón DAO.

## 🏗️ Arquitectura

El proyecto sigue una arquitectura por capas simple:

```
src/main/java/com/gestorproyectos/
├── controller/     # Controladores REST (exponen las rutas)
├── dao/           # Data Access Object (acceso a datos MySQL)
└── model/         # Modelos/Entidades del dominio
```

### Justificación de la Arquitectura

- **DAO**: Separa la lógica de acceso a MySQL sin agregar complejidad innecesaria
- **Controlador**: Solo expone rutas y recibe solicitudes HTTP
- **Modelo**: Representa las entidades del dominio (tablas de la base de datos)
- **Sin servicios avanzados**: Se mantiene simple y formativo, siguiendo el patrón DAO tradicional

## 📦 Entidades del Sistema

### Usuario
- `idUsuario`: Identificador único
- `nombre`: Nombre del usuario
- `apellidos`: Apellidos del usuario
- `correo`: Correo electrónico (único)
- `telefono`: Número de teléfono
- `rol`: Rol del usuario (ADMIN, GESTOR, INVITADO)

### Proyecto
- `idProyecto`: Identificador único
- `fechaRadicacion`: Fecha en que se radicó el proyecto
- `nombre`: Nombre del proyecto
- `descripcion`: Descripción detallada
- `estadoActual`: Estado actual (RADICADO, EN_PROCESO, FINALIZADO, RECHAZADO)
- `fechaUltimaActualizacion`: Fecha de la última actualización
- `idGestorAsignado`: ID del usuario gestor asignado (FK)

### Trazabilidad
- `idTrazabilidad`: Identificador único
- `idProyecto`: ID del proyecto (FK)
- `observacion`: Observación sobre el cambio de estado
- `usuarioQueRealiza`: ID del usuario que realizó el cambio (FK)
- `fecha`: Fecha y hora del registro
- `nuevoEstado`: Nuevo estado aplicado al proyecto

## 🔌 API REST Endpoints

### Usuarios

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/usuarios` | Listar todos los usuarios |
| POST | `/api/usuarios` | Crear un nuevo usuario |
| GET | `/api/usuarios/{id}` | Buscar usuario por ID |

### Proyectos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/proyectos` | Listar todos los proyectos |
| POST | `/api/proyectos` | Crear un nuevo proyecto |
| GET | `/api/proyectos/{id}` | Buscar proyecto por ID |

### Trazabilidad

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/trazabilidad` | Registrar cambio de estado |
| GET | `/api/trazabilidad/proyecto/{id}` | Consultar historial de un proyecto |

## 🗄️ Base de Datos

### Requisitos
- MySQL 8.0 o superior
- Base de datos: `gestor_proyectos`

### Configuración

1. Crear la base de datos ejecutando el script:
```bash
mysql -u root -p < database/schema.sql
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestor_proyectos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java 17 o superior
- Maven 3.6 o superior (o usar Maven Wrapper incluido)
- MySQL 8.0 o superior
- Node.js 18+ y npm (para el frontend)

### Pasos para Ejecutar

1. **Clonar o descargar el proyecto**

2. **Crear la base de datos**:
```bash
mysql -u root -p < database/schema.sql
```

3. **Configurar la conexión a la base de datos** en `src/main/resources/application.properties`

4. **Compilar el proyecto**:
```bash
./mvnw clean install
```

5. **Ejecutar la aplicación**:
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`


## 📝 Ejemplos de Uso

### Crear un Usuario
```bash
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Juan",
  "apellidos": "Pérez",
  "correo": "juan.perez@example.com",
  "telefono": "3001234567",
  "rol": "GESTOR"
}
```

### Crear un Proyecto
```bash
POST http://localhost:8080/api/proyectos
Content-Type: application/json

{
  "nombre": "Proyecto de Desarrollo Web",
  "descripcion": "Desarrollo de aplicación web para gestión de inventarios",
  "idGestorAsignado": 1
}
```

### Registrar Trazabilidad (Cambio de Estado)
```bash
POST http://localhost:8080/api/trazabilidad
Content-Type: application/json

{
  "idProyecto": 1,
  "observacion": "Proyecto iniciado, asignación de recursos completada",
  "usuarioQueRealiza": 1,
  "nuevoEstado": "EN_PROCESO"
}
```

### Consultar Historial de un Proyecto
```bash
GET http://localhost:8080/api/trazabilidad/proyecto/1
```

## 🔒 Reglas de Negocio

1. **Actualización Automática del Estado**: Al registrar una trazabilidad, el estado del proyecto se actualiza automáticamente.

2. **Fechas Automáticas**: Las fechas de radicación, actualización y trazabilidad se generan automáticamente usando la fecha del sistema.

3. **Estado Inicial**: Todo proyecto nuevo se crea con estado `RADICADO`.

4. **Validación de Gestor**: El gestor asignado debe existir previamente en la base de datos.

5. **Validación de Estados**: Solo se permiten los estados definidos en el enum `EstadoProyecto`.

## 📚 Documentación Adicional

- [Diagrama Entidad-Relación (DER)](docs/DER.md)
- [Pseudocódigo y Diagramas de Flujo](docs/Pseudocodigo.md)
- [Script SQL de Base de Datos](database/schema.sql)

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.2.0**: Framework principal
- **Spring JDBC**: Para acceso a datos
- **MySQL Connector**: Driver de base de datos
- **Maven**: Gestión de dependencias
- **Java 17**: Lenguaje de programación


## 📄 Licencia

Este proyecto es parte de un taller formativo del SENA - Centro de gestión de mercados logística y tecnologías de la información.

## 👨‍💻 Autor

Desarrollado como parte del programa **Análisis y Desarrollo de Software del SENA**.

---

**Instructor**: Jesús Ropero Barbosa  
**Email**: jropero@sena.edu.co

