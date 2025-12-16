package com.gestorproyectos.model;

import java.time.LocalDateTime;

/**
 * Clase que representa la entidad Proyecto
 */
public class Proyecto {
    private Integer idProyecto;
    private LocalDateTime fechaRadicacion;
    private String nombre;
    private String descripcion;
    private EstadoProyecto estadoActual;
    private LocalDateTime fechaUltimaActualizacion;
    private Integer idGestorAsignado;

    // Constructores
    public Proyecto() {
    }

    public Proyecto(Integer idProyecto, LocalDateTime fechaRadicacion, String nombre, String descripcion,
                    EstadoProyecto estadoActual, LocalDateTime fechaUltimaActualizacion, Integer idGestorAsignado) {
        this.idProyecto = idProyecto;
        this.fechaRadicacion = fechaRadicacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estadoActual = estadoActual;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.idGestorAsignado = idGestorAsignado;
    }

    // Getters y Setters
    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public LocalDateTime getFechaRadicacion() {
        return fechaRadicacion;
    }

    public void setFechaRadicacion(LocalDateTime fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoProyecto getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoProyecto estadoActual) {
        this.estadoActual = estadoActual;
    }

    public LocalDateTime getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public Integer getIdGestorAsignado() {
        return idGestorAsignado;
    }

    public void setIdGestorAsignado(Integer idGestorAsignado) {
        this.idGestorAsignado = idGestorAsignado;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "idProyecto=" + idProyecto +
                ", fechaRadicacion=" + fechaRadicacion +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estadoActual=" + estadoActual +
                ", fechaUltimaActualizacion=" + fechaUltimaActualizacion +
                ", idGestorAsignado=" + idGestorAsignado +
                '}';
    }
}

