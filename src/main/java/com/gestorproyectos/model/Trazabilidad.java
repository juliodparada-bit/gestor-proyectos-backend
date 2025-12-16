package com.gestorproyectos.model;

import java.time.LocalDateTime;

/**
 * Clase que representa la entidad Trazabilidad (histórico de actividad)
 */
public class Trazabilidad {
    private Integer idTrazabilidad;
    private Integer idProyecto;
    private String observacion;
    private Integer usuarioQueRealiza;
    private LocalDateTime fecha;
    private EstadoProyecto nuevoEstado;

    // Constructores
    public Trazabilidad() {
    }

    public Trazabilidad(Integer idTrazabilidad, Integer idProyecto, String observacion,
                        Integer usuarioQueRealiza, LocalDateTime fecha, EstadoProyecto nuevoEstado) {
        this.idTrazabilidad = idTrazabilidad;
        this.idProyecto = idProyecto;
        this.observacion = observacion;
        this.usuarioQueRealiza = usuarioQueRealiza;
        this.fecha = fecha;
        this.nuevoEstado = nuevoEstado;
    }

    // Getters y Setters
    public Integer getIdTrazabilidad() {
        return idTrazabilidad;
    }

    public void setIdTrazabilidad(Integer idTrazabilidad) {
        this.idTrazabilidad = idTrazabilidad;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getUsuarioQueRealiza() {
        return usuarioQueRealiza;
    }

    public void setUsuarioQueRealiza(Integer usuarioQueRealiza) {
        this.usuarioQueRealiza = usuarioQueRealiza;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoProyecto getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(EstadoProyecto nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    @Override
    public String toString() {
        return "Trazabilidad{" +
                "idTrazabilidad=" + idTrazabilidad +
                ", idProyecto=" + idProyecto +
                ", observacion='" + observacion + '\'' +
                ", usuarioQueRealiza=" + usuarioQueRealiza +
                ", fecha=" + fecha +
                ", nuevoEstado=" + nuevoEstado +
                '}';
    }
}

