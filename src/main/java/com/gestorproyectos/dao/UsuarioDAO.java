package com.gestorproyectos.dao;

import com.gestorproyectos.model.RolUsuario;
import com.gestorproyectos.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO para la entidad Usuario
 * Implementa las operaciones CRUD sobre la tabla usuario
 */
@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Mapea un ResultSet a un objeto Usuario
     */
    private final RowMapper<Usuario> usuarioRowMapper = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setApellidos(rs.getString("apellidos"));
            usuario.setCorreo(rs.getString("correo"));
            usuario.setTelefono(rs.getString("telefono"));
            usuario.setRol(RolUsuario.valueOf(rs.getString("rol")));
            return usuario;
        }
    };

    /**
     * Crea un nuevo usuario en la base de datos
     */
    public Usuario crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, apellidos, correo, telefono, rol) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getRol().name());

        // Obtener el ID generado
        Integer idGenerado = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        usuario.setIdUsuario(idGenerado);
        return usuario;
    }

    /**
     * Lista todos los usuarios
     */
    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuario ORDER BY id_usuario";
        return jdbcTemplate.query(sql, usuarioRowMapper);
    }

    /**
     * Busca un usuario por su ID
     */
    public Usuario buscarPorId(Integer idUsuario) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try {
            return jdbcTemplate.queryForObject(sql, usuarioRowMapper, idUsuario);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si existe un usuario con el ID dado
     */
    public boolean existe(Integer idUsuario) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id_usuario = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idUsuario);
        return count != null && count > 0;
    }
}

