package com.gestorproyectos.dao;

import com.gestorproyectos.model.EstadoProyecto;
import com.gestorproyectos.model.Proyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO para la entidad Proyecto
 * Implementa las operaciones CRUD sobre la tabla proyecto
 */
@Repository
public class ProyectoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UsuarioDAO usuarioDAO;

    /**
     * Mapea un ResultSet a un objeto Proyecto
     */
    private final RowMapper<Proyecto> proyectoRowMapper = new RowMapper<Proyecto>() {
        @Override
        public Proyecto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Proyecto proyecto = new Proyecto();
            proyecto.setIdProyecto(rs.getInt("id_proyecto"));
            
            if (rs.getTimestamp("fecha_radicacion") != null) {
                proyecto.setFechaRadicacion(rs.getTimestamp("fecha_radicacion").toLocalDateTime());
            }
            
            proyecto.setNombre(rs.getString("nombre"));
            proyecto.setDescripcion(rs.getString("descripcion"));
            proyecto.setEstadoActual(EstadoProyecto.valueOf(rs.getString("estado_actual")));
            
            if (rs.getTimestamp("fecha_ultima_actualizacion") != null) {
                proyecto.setFechaUltimaActualizacion(rs.getTimestamp("fecha_ultima_actualizacion").toLocalDateTime());
            }
            
            proyecto.setIdGestorAsignado(rs.getInt("id_gestor_asignado"));
            return proyecto;
        }
    };

    /**
     * Crea un nuevo proyecto en la base de datos
     * Valida que el gestor asignado exista
     */
    public Proyecto crear(Proyecto proyecto) {
        // Validar que el gestor asignado exista
        if (!usuarioDAO.existe(proyecto.getIdGestorAsignado())) {
            throw new IllegalArgumentException("El gestor asignado no existe en la base de datos");
        }

        LocalDateTime ahora = LocalDateTime.now();
        proyecto.setFechaRadicacion(ahora);
        proyecto.setFechaUltimaActualizacion(ahora);
        proyecto.setEstadoActual(EstadoProyecto.RADICADO);

        String sql = "INSERT INTO proyecto (fecha_radicacion, nombre, descripcion, estado_actual, " +
                     "fecha_ultima_actualizacion, id_gestor_asignado) VALUES (?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
                java.sql.Timestamp.valueOf(proyecto.getFechaRadicacion()),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getEstadoActual().name(),
                java.sql.Timestamp.valueOf(proyecto.getFechaUltimaActualizacion()),
                proyecto.getIdGestorAsignado());

        // Obtener el ID generado
        Integer idGenerado = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        proyecto.setIdProyecto(idGenerado);
        return proyecto;
    }

    /**
     * Lista todos los proyectos
     */
    public List<Proyecto> listar() {
        String sql = "SELECT * FROM proyecto ORDER BY id_proyecto";
        return jdbcTemplate.query(sql, proyectoRowMapper);
    }

    /**
     * Busca un proyecto por su ID
     */
    public Proyecto buscarPorId(Integer idProyecto) {
        String sql = "SELECT * FROM proyecto WHERE id_proyecto = ?";
        try {
            return jdbcTemplate.queryForObject(sql, proyectoRowMapper, idProyecto);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Actualiza el estado actual y la fecha de última actualización del proyecto
     */
    public void actualizarEstado(Integer idProyecto, EstadoProyecto nuevoEstado, LocalDateTime fechaActualizacion) {
        String sql = "UPDATE proyecto SET estado_actual = ?, fecha_ultima_actualizacion = ? WHERE id_proyecto = ?";
        jdbcTemplate.update(sql,
                nuevoEstado.name(),
                java.sql.Timestamp.valueOf(fechaActualizacion),
                idProyecto);
    }

    /**
     * Verifica si existe un proyecto con el ID dado
     */
    public boolean existe(Integer idProyecto) {
        String sql = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idProyecto);
        return count != null && count > 0;
    }
}

