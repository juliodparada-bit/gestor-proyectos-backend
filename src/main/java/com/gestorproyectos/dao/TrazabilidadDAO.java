package com.gestorproyectos.dao;

import com.gestorproyectos.model.EstadoProyecto;
import com.gestorproyectos.model.Trazabilidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO para la entidad Trazabilidad
 * Implementa las operaciones CRUD sobre la tabla trazabilidad
 */
@Repository
public class TrazabilidadDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProyectoDAO proyectoDAO;

    /**
     * Mapea un ResultSet a un objeto Trazabilidad
     */
    private final RowMapper<Trazabilidad> trazabilidadRowMapper = new RowMapper<Trazabilidad>() {
        @Override
        public Trazabilidad mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trazabilidad trazabilidad = new Trazabilidad();
            trazabilidad.setIdTrazabilidad(rs.getInt("id_trazabilidad"));
            trazabilidad.setIdProyecto(rs.getInt("id_proyecto"));
            trazabilidad.setObservacion(rs.getString("observacion"));
            trazabilidad.setUsuarioQueRealiza(rs.getInt("usuario_que_realiza"));
            
            if (rs.getTimestamp("fecha") != null) {
                trazabilidad.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
            }
            
            trazabilidad.setNuevoEstado(EstadoProyecto.valueOf(rs.getString("nuevo_estado")));
            return trazabilidad;
        }
    };

    /**
     * Registra un cambio de estado (trazabilidad)
     * Actualiza automáticamente el estado del proyecto y su fecha de última actualización
     */
    @Transactional
    public Trazabilidad registrar(Trazabilidad trazabilidad) {
        // Validar que el proyecto exista
        if (!proyectoDAO.existe(trazabilidad.getIdProyecto())) {
            throw new IllegalArgumentException("El proyecto no existe en la base de datos");
        }

        // Validar que el usuario que realiza la acción exista
        // (asumiendo que tenemos acceso a UsuarioDAO, si no, se puede validar en el controlador)
        
        // Establecer la fecha actual
        LocalDateTime ahora = LocalDateTime.now();
        trazabilidad.setFecha(ahora);

        // Insertar el registro de trazabilidad
        String sql = "INSERT INTO trazabilidad (id_proyecto, observacion, usuario_que_realiza, fecha, nuevo_estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
                trazabilidad.getIdProyecto(),
                trazabilidad.getObservacion(),
                trazabilidad.getUsuarioQueRealiza(),
                java.sql.Timestamp.valueOf(trazabilidad.getFecha()),
                trazabilidad.getNuevoEstado().name());

        // Obtener el ID generado
        Integer idGenerado = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        trazabilidad.setIdTrazabilidad(idGenerado);

        // Actualizar el estado actual del proyecto y su fecha de última actualización
        proyectoDAO.actualizarEstado(trazabilidad.getIdProyecto(), 
                                     trazabilidad.getNuevoEstado(), 
                                     ahora);

        return trazabilidad;
    }

    /**
     * Consulta el historial de trazabilidad de un proyecto
     */
    public List<Trazabilidad> consultarPorProyecto(Integer idProyecto) {
        String sql = "SELECT * FROM trazabilidad WHERE id_proyecto = ? ORDER BY fecha ASC";
        return jdbcTemplate.query(sql, trazabilidadRowMapper, idProyecto);
    }

    /**
     * Busca una trazabilidad por su ID
     */
    public Trazabilidad buscarPorId(Integer idTrazabilidad) {
        String sql = "SELECT * FROM trazabilidad WHERE id_trazabilidad = ?";
        try {
            return jdbcTemplate.queryForObject(sql, trazabilidadRowMapper, idTrazabilidad);
        } catch (Exception e) {
            return null;
        }
    }
}

