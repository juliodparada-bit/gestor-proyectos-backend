-- Script SQL para crear la base de datos y las tablas del sistema Gestor de Proyectos

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gestor_proyectos;
USE gestor_proyectos;

-- Tabla: usuario
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    rol ENUM('ADMIN', 'GESTOR', 'INVITADO') NOT NULL,
    INDEX idx_correo (correo),
    INDEX idx_rol (rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: proyecto
CREATE TABLE IF NOT EXISTS proyecto (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    fecha_radicacion DATETIME NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    estado_actual ENUM('RADICADO', 'EN_PROCESO', 'FINALIZADO', 'RECHAZADO') NOT NULL DEFAULT 'RADICADO',
    fecha_ultima_actualizacion DATETIME NOT NULL,
    id_gestor_asignado INT NOT NULL,
    FOREIGN KEY (id_gestor_asignado) REFERENCES usuario(id_usuario) ON DELETE RESTRICT,
    INDEX idx_estado (estado_actual),
    INDEX idx_gestor (id_gestor_asignado),
    INDEX idx_fecha_radicacion (fecha_radicacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: trazabilidad
CREATE TABLE IF NOT EXISTS trazabilidad (
    id_trazabilidad INT AUTO_INCREMENT PRIMARY KEY,
    id_proyecto INT NOT NULL,
    observacion TEXT NOT NULL,
    usuario_que_realiza INT NOT NULL,
    fecha DATETIME NOT NULL,
    nuevo_estado ENUM('RADICADO', 'EN_PROCESO', 'FINALIZADO', 'RECHAZADO') NOT NULL,
    FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto) ON DELETE CASCADE,
    FOREIGN KEY (usuario_que_realiza) REFERENCES usuario(id_usuario) ON DELETE RESTRICT,
    INDEX idx_proyecto (id_proyecto),
    INDEX idx_usuario (usuario_que_realiza),
    INDEX idx_fecha (fecha),
    INDEX idx_estado (nuevo_estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

