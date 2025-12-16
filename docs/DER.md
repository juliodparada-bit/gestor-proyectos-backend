# Diagrama Entidad-Relación (DER) - Sistema Gestor de Proyectos

## Relaciones entre Entidades

```
┌─────────────────┐
│     USUARIO     │
│─────────────────│
│ id_usuario (PK) │
│ nombre          │
│ apellidos       │
│ correo          │
│ telefono        │
│ rol             │
└────────┬────────┘
         │
         │ 1
         │
         │ N
         │
         │ id_gestor_asignado
         │
┌────────▼────────┐
│    PROYECTO     │
│─────────────────│
│ id_proyecto (PK)│
│ fecha_radicacion│
│ nombre          │
│ descripcion     │
│ estado_actual   │
│ fecha_ultima_   │
│   actualizacion │
│ id_gestor_      │
│   asignado (FK) │
└────────┬────────┘
         │
         │ 1
         │
         │ N
         │
         │ id_proyecto
         │
┌────────▼────────┐
│  TRAZABILIDAD   │
│─────────────────│
│ id_trazabilidad │
│   (PK)          │
│ id_proyecto (FK)│
│ observacion     │
│ usuario_que_    │
│   realiza (FK)  │
│ fecha           │
│ nuevo_estado    │
└─────────────────┘
         ▲
         │
         │ N
         │
         │ usuario_que_realiza
         │
         │ 1
         │
┌────────┴────────┐
│     USUARIO     │
│  (relación con  │
│   trazabilidad) │
└─────────────────┘
```

## Descripción de Relaciones

### 1. Usuario (1) ──── (N) Proyecto
- **Tipo**: Uno a Muchos
- **Descripción**: Un usuario (con rol GESTOR) puede estar asignado a múltiples proyectos, pero cada proyecto tiene un único gestor asignado.
- **Clave Foránea**: `proyecto.id_gestor_asignado` → `usuario.id_usuario`
- **Restricción**: ON DELETE RESTRICT (no se puede eliminar un usuario si tiene proyectos asignados)

### 2. Proyecto (1) ──── (N) Trazabilidad
- **Tipo**: Uno a Muchos
- **Descripción**: Un proyecto puede tener múltiples registros de trazabilidad (historial de cambios de estado).
- **Clave Foránea**: `trazabilidad.id_proyecto` → `proyecto.id_proyecto`
- **Restricción**: ON DELETE CASCADE (si se elimina un proyecto, se eliminan sus trazabilidades)

### 3. Usuario (1) ──── (N) Trazabilidad
- **Tipo**: Uno a Muchos
- **Descripción**: Un usuario puede realizar múltiples registros de trazabilidad (cambios de estado en diferentes proyectos).
- **Clave Foránea**: `trazabilidad.usuario_que_realiza` → `usuario.id_usuario`
- **Restricción**: ON DELETE RESTRICT (no se puede eliminar un usuario si tiene trazabilidades registradas)

## Estructura de Tablas

### Tabla: usuario
- **id_usuario** (PK, INT, AUTO_INCREMENT): Identificador único del usuario
- **nombre** (VARCHAR(100), NOT NULL): Nombre del usuario
- **apellidos** (VARCHAR(100), NOT NULL): Apellidos del usuario
- **correo** (VARCHAR(150), NOT NULL, UNIQUE): Correo electrónico único
- **telefono** (VARCHAR(20), NOT NULL): Número de teléfono
- **rol** (ENUM('ADMIN', 'GESTOR', 'INVITADO'), NOT NULL): Rol del usuario en el sistema

### Tabla: proyecto
- **id_proyecto** (PK, INT, AUTO_INCREMENT): Identificador único del proyecto
- **fecha_radicacion** (DATETIME, NOT NULL): Fecha en que se radicó el proyecto
- **nombre** (VARCHAR(200), NOT NULL): Nombre del proyecto
- **descripcion** (TEXT, NOT NULL): Descripción detallada del proyecto
- **estado_actual** (ENUM('RADICADO', 'EN_PROCESO', 'FINALIZADO', 'RECHAZADO'), NOT NULL): Estado actual del proyecto
- **fecha_ultima_actualizacion** (DATETIME, NOT NULL): Fecha de la última actualización del estado
- **id_gestor_asignado** (FK, INT, NOT NULL): Referencia al usuario gestor asignado

### Tabla: trazabilidad
- **id_trazabilidad** (PK, INT, AUTO_INCREMENT): Identificador único del registro de trazabilidad
- **id_proyecto** (FK, INT, NOT NULL): Referencia al proyecto
- **observacion** (TEXT, NOT NULL): Observación o comentario sobre el cambio de estado
- **usuario_que_realiza** (FK, INT, NOT NULL): Referencia al usuario que realizó el cambio
- **fecha** (DATETIME, NOT NULL): Fecha y hora del registro
- **nuevo_estado** (ENUM('RADICADO', 'EN_PROCESO', 'FINALIZADO', 'RECHAZADO'), NOT NULL): Nuevo estado aplicado al proyecto

## Índices

### Tabla usuario
- Índice en `correo` (para búsquedas rápidas)
- Índice en `rol` (para filtros por rol)

### Tabla proyecto
- Índice en `estado_actual` (para filtros por estado)
- Índice en `id_gestor_asignado` (para búsquedas por gestor)
- Índice en `fecha_radicacion` (para ordenamientos temporales)

### Tabla trazabilidad
- Índice en `id_proyecto` (para consultas de historial)
- Índice en `usuario_que_realiza` (para consultas por usuario)
- Índice en `fecha` (para ordenamientos temporales)
- Índice en `nuevo_estado` (para filtros por estado)

