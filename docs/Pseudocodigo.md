# Pseudocódigo y Diagramas de Flujo - Sistema Gestor de Proyectos

## 1. Proceso: Registrar Trazabilidad (Cambio de Estado)

### Diagrama de Flujo

```
INICIO
  │
  ▼
[Recibir solicitud POST /api/trazabilidad]
  │
  ▼
[Validar datos de entrada]
  │
  ├─¿idProyecto válido? ──NO──► [Retornar error 400]
  │                              │
  ├─¿observacion válida? ──NO──► [Retornar error 400]
  │                              │
  ├─¿usuarioQueRealiza válido? ──NO──► [Retornar error 400]
  │                                    │
  └─¿nuevoEstado válido? ──NO──► [Retornar error 400]
                                  │
                                  ▼
                                 FIN (Error)
  │
  ▼
[Validar que el proyecto existe]
  │
  ├─¿Proyecto existe? ──NO──► [Retornar error 400]
  │                           │
  └─¿Usuario existe? ──NO──► [Retornar error 400]
                              │
                              ▼
                             FIN (Error)
  │
  ▼
[Obtener fecha actual del sistema]
  │
  ▼
[INICIAR TRANSACCIÓN]
  │
  ▼
[Insertar registro en tabla trazabilidad]
  │
  │  INSERT INTO trazabilidad 
  │  (id_proyecto, observacion, usuario_que_realiza, fecha, nuevo_estado)
  │  VALUES (?, ?, ?, NOW(), ?)
  │
  ▼
[Actualizar estado del proyecto]
  │
  │  UPDATE proyecto 
  │  SET estado_actual = nuevoEstado,
  │      fecha_ultima_actualizacion = NOW()
  │  WHERE id_proyecto = ?
  │
  ▼
[CONFIRMAR TRANSACCIÓN]
  │
  ▼
[Retornar trazabilidad creada con código 201]
  │
  ▼
FIN (Éxito)
```

### Pseudocódigo

```pseudocodigo
FUNCIÓN registrarTrazabilidad(trazabilidad: Trazabilidad) RETORNA Trazabilidad
    INICIO
    
    // Validaciones de entrada
    SI trazabilidad.idProyecto ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "El ID del proyecto es obligatorio"
    FIN SI
    
    SI trazabilidad.observacion ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "La observación es obligatoria"
    FIN SI
    
    SI trazabilidad.usuarioQueRealiza ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "El usuario que realiza la acción es obligatorio"
    FIN SI
    
    SI trazabilidad.nuevoEstado ES NULL ENTONCES
        LANZAR EXCEPCIÓN "El nuevo estado es obligatorio"
    FIN SI
    
    // Validar existencia de entidades relacionadas
    SI NO proyectoDAO.existe(trazabilidad.idProyecto) ENTONCES
        LANZAR EXCEPCIÓN "El proyecto no existe en la base de datos"
    FIN SI
    
    SI NO usuarioDAO.existe(trazabilidad.usuarioQueRealiza) ENTONCES
        LANZAR EXCEPCIÓN "El usuario que realiza la acción no existe"
    FIN SI
    
    // Establecer fecha actual
    fechaActual = OBTENER_FECHA_ACTUAL_DEL_SISTEMA()
    trazabilidad.fecha = fechaActual
    
    // INICIAR TRANSACCIÓN
    INICIAR_TRANSACCION()
    
    TRY
        // Insertar registro de trazabilidad
        sql = "INSERT INTO trazabilidad (id_proyecto, observacion, usuario_que_realiza, fecha, nuevo_estado) 
               VALUES (?, ?, ?, ?, ?)"
        
        EJECUTAR_SQL(sql, 
                     trazabilidad.idProyecto,
                     trazabilidad.observacion,
                     trazabilidad.usuarioQueRealiza,
                     fechaActual,
                     trazabilidad.nuevoEstado.name())
        
        // Obtener ID generado
        trazabilidad.idTrazabilidad = OBTENER_ULTIMO_ID_GENERADO()
        
        // Actualizar estado del proyecto
        proyectoDAO.actualizarEstado(
            trazabilidad.idProyecto,
            trazabilidad.nuevoEstado,
            fechaActual
        )
        
        // CONFIRMAR TRANSACCIÓN
        CONFIRMAR_TRANSACCION()
        
        RETORNAR trazabilidad
        
    CATCH (error)
        // REVERTIR TRANSACCIÓN en caso de error
        REVERTIR_TRANSACCION()
        LANZAR EXCEPCIÓN error
    FIN TRY
    
    FIN
FIN FUNCIÓN
```

## 2. Proceso: Crear Proyecto

### Diagrama de Flujo

```
INICIO
  │
  ▼
[Recibir solicitud POST /api/proyectos]
  │
  ▼
[Validar datos de entrada]
  │
  ├─¿nombre válido? ──NO──► [Retornar error 400]
  │                        │
  ├─¿descripcion válida? ──NO──► [Retornar error 400]
  │                              │
  └─¿idGestorAsignado válido? ──NO──► [Retornar error 400]
                                      │
                                      ▼
                                     FIN (Error)
  │
  ▼
[Validar que el gestor asignado existe]
  │
  ├─¿Gestor existe? ──NO──► [Retornar error 400]
  │                        │
  └─                        │
                           ▼
                          FIN (Error)
  │
  ▼
[Obtener fecha actual del sistema]
  │
  ▼
[Establecer valores por defecto]
  │
  │  fechaRadicacion = fechaActual
  │  fechaUltimaActualizacion = fechaActual
  │  estadoActual = RADICADO
  │
  ▼
[Insertar proyecto en la base de datos]
  │
  │  INSERT INTO proyecto 
  │  (fecha_radicacion, nombre, descripcion, estado_actual, 
  │   fecha_ultima_actualizacion, id_gestor_asignado)
  │  VALUES (?, ?, ?, 'RADICADO', ?, ?)
  │
  ▼
[Obtener ID generado]
  │
  ▼
[Retornar proyecto creado con código 201]
  │
  ▼
FIN (Éxito)
```

### Pseudocódigo

```pseudocodigo
FUNCIÓN crearProyecto(proyecto: Proyecto) RETORNA Proyecto
    INICIO
    
    // Validaciones de entrada
    SI proyecto.nombre ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "El nombre del proyecto es obligatorio"
    FIN SI
    
    SI proyecto.descripcion ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "La descripción del proyecto es obligatoria"
    FIN SI
    
    SI proyecto.idGestorAsignado ES NULL ENTONCES
        LANZAR EXCEPCIÓN "El gestor asignado es obligatorio"
    FIN SI
    
    // Validar que el gestor asignado existe
    SI NO usuarioDAO.existe(proyecto.idGestorAsignado) ENTONCES
        LANZAR EXCEPCIÓN "El gestor asignado no existe en la base de datos"
    FIN SI
    
    // Establecer valores por defecto
    fechaActual = OBTENER_FECHA_ACTUAL_DEL_SISTEMA()
    proyecto.fechaRadicacion = fechaActual
    proyecto.fechaUltimaActualizacion = fechaActual
    proyecto.estadoActual = EstadoProyecto.RADICADO
    
    // Insertar proyecto
    sql = "INSERT INTO proyecto (fecha_radicacion, nombre, descripcion, estado_actual, 
                                 fecha_ultima_actualizacion, id_gestor_asignado) 
           VALUES (?, ?, ?, ?, ?, ?)"
    
    EJECUTAR_SQL(sql,
                 fechaActual,
                 proyecto.nombre,
                 proyecto.descripcion,
                 proyecto.estadoActual.name(),
                 fechaActual,
                 proyecto.idGestorAsignado)
    
    // Obtener ID generado
    proyecto.idProyecto = OBTENER_ULTIMO_ID_GENERADO()
    
    RETORNAR proyecto
    
    FIN
FIN FUNCIÓN
```

## 3. Proceso: Consultar Historial de Trazabilidad

### Diagrama de Flujo

```
INICIO
  │
  ▼
[Recibir solicitud GET /api/trazabilidad/proyecto/{id}]
  │
  ▼
[Validar que el ID del proyecto es válido]
  │
  ├─¿ID válido? ──NO──► [Retornar error 400]
  │                     │
  └─                     │
                        ▼
                       FIN (Error)
  │
  ▼
[Consultar trazabilidades del proyecto]
  │
  │  SELECT * FROM trazabilidad 
  │  WHERE id_proyecto = ?
  │  ORDER BY fecha ASC
  │
  ▼
[Retornar lista de trazabilidades con código 200]
  │
  ▼
FIN (Éxito)
```

### Pseudocódigo

```pseudocodigo
FUNCIÓN consultarTrazabilidadPorProyecto(idProyecto: Integer) RETORNA Lista<Trazabilidad>
    INICIO
    
    // Validar entrada
    SI idProyecto ES NULL O idProyecto <= 0 ENTONCES
        LANZAR EXCEPCIÓN "El ID del proyecto es inválido"
    FIN SI
    
    // Consultar trazabilidades
    sql = "SELECT * FROM trazabilidad 
           WHERE id_proyecto = ? 
           ORDER BY fecha ASC"
    
    listaTrazabilidades = EJECUTAR_CONSULTA_SQL(sql, idProyecto)
    
    RETORNAR listaTrazabilidades
    
    FIN
FIN FUNCIÓN
```

## 4. Proceso: Crear Usuario

### Diagrama de Flujo

```
INICIO
  │
  ▼
[Recibir solicitud POST /api/usuarios]
  │
  ▼
[Validar datos de entrada]
  │
  ├─¿nombre válido? ──NO──► [Retornar error 400]
  │                        │
  ├─¿apellidos válidos? ──NO──► [Retornar error 400]
  │                             │
  ├─¿correo válido? ──NO──► [Retornar error 400]
  │                        │
  ├─¿telefono válido? ──NO──► [Retornar error 400]
  │                          │
  └─¿rol válido? ──NO──► [Retornar error 400]
                          │
                          ▼
                         FIN (Error)
  │
  ▼
[Validar que el rol es uno de los permitidos]
  │
  ├─¿rol ∈ {ADMIN, GESTOR, INVITADO}? ──NO──► [Retornar error 400]
  │                                          │
  └─                                          │
                                             ▼
                                            FIN (Error)
  │
  ▼
[Insertar usuario en la base de datos]
  │
  │  INSERT INTO usuario 
  │  (nombre, apellidos, correo, telefono, rol)
  │  VALUES (?, ?, ?, ?, ?)
  │
  ▼
[Obtener ID generado]
  │
  ▼
[Retornar usuario creado con código 201]
  │
  ▼
FIN (Éxito)
```

### Pseudocódigo

```pseudocodigo
FUNCIÓN crearUsuario(usuario: Usuario) RETORNA Usuario
    INICIO
    
    // Validaciones de entrada
    SI usuario.nombre ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "El nombre es obligatorio"
    FIN SI
    
    SI usuario.apellidos ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "Los apellidos son obligatorios"
    FIN SI
    
    SI usuario.correo ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "El correo es obligatorio"
    FIN SI
    
    SI usuario.telefono ES NULL O VACÍO ENTONCES
        LANZAR EXCEPCIÓN "El teléfono es obligatorio"
    FIN SI
    
    SI usuario.rol ES NULL ENTONCES
        LANZAR EXCEPCIÓN "El rol es obligatorio"
    FIN SI
    
    // Validar que el rol es válido
    SI usuario.rol NO ESTÁ EN {ADMIN, GESTOR, INVITADO} ENTONCES
        LANZAR EXCEPCIÓN "El rol solo puede ser ADMIN, GESTOR o INVITADO"
    FIN SI
    
    // Insertar usuario
    sql = "INSERT INTO usuario (nombre, apellidos, correo, telefono, rol) 
           VALUES (?, ?, ?, ?, ?)"
    
    EJECUTAR_SQL(sql,
                 usuario.nombre,
                 usuario.apellidos,
                 usuario.correo,
                 usuario.telefono,
                 usuario.rol.name())
    
    // Obtener ID generado
    usuario.idUsuario = OBTENER_ULTIMO_ID_GENERADO()
    
    RETORNAR usuario
    
    FIN
FIN FUNCIÓN
```

## 5. Reglas de Negocio Implementadas

### Regla 1: Actualización Automática del Estado del Proyecto
```
CUANDO se registra una trazabilidad ENTONCES
    ACTUALIZAR proyecto.estadoActual = trazabilidad.nuevoEstado
    ACTUALIZAR proyecto.fechaUltimaActualizacion = fechaActual
FIN CUANDO
```

### Regla 2: Generación Automática de Fechas
```
CUANDO se crea un proyecto ENTONCES
    ESTABLECER proyecto.fechaRadicacion = fechaActual
    ESTABLECER proyecto.fechaUltimaActualizacion = fechaActual
FIN CUANDO

CUANDO se registra una trazabilidad ENTONCES
    ESTABLECER trazabilidad.fecha = fechaActual
FIN CUANDO
```

### Regla 3: Estado Inicial del Proyecto
```
CUANDO se crea un proyecto ENTONCES
    ESTABLECER proyecto.estadoActual = RADICADO
FIN CUANDO
```

### Regla 4: Validación de Gestor Asignado
```
ANTES DE crear un proyecto VERIFICAR
    SI usuarioDAO.existe(proyecto.idGestorAsignado) ENTONCES
        PERMITIR creación
    SINO
        RECHAZAR con error "El gestor asignado no existe"
    FIN SI
FIN VERIFICAR
```

