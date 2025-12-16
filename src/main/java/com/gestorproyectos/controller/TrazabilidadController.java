package com.gestorproyectos.controller;

import com.gestorproyectos.dao.TrazabilidadDAO;
import com.gestorproyectos.dao.UsuarioDAO;
import com.gestorproyectos.model.Trazabilidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de trazabilidad
 */
@RestController
@RequestMapping("/api/trazabilidad")
@CrossOrigin(origins = "*")
public class TrazabilidadController {

    @Autowired
    private TrazabilidadDAO trazabilidadDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    /**
     * Registra un cambio de estado (trazabilidad)
     * POST /api/trazabilidad
     */
    @PostMapping
    public ResponseEntity<?> registrarTrazabilidad(@RequestBody Trazabilidad trazabilidad) {
        try {
            // Validaciones básicas
            if (trazabilidad.getIdProyecto() == null) {
                return ResponseEntity.badRequest().body("El ID del proyecto es obligatorio");
            }
            if (trazabilidad.getObservacion() == null || trazabilidad.getObservacion().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La observación es obligatoria");
            }
            if (trazabilidad.getUsuarioQueRealiza() == null) {
                return ResponseEntity.badRequest().body("El usuario que realiza la acción es obligatorio");
            }
            if (trazabilidad.getNuevoEstado() == null) {
                return ResponseEntity.badRequest().body("El nuevo estado es obligatorio");
            }

            // Validar que el usuario existe
            if (!usuarioDAO.existe(trazabilidad.getUsuarioQueRealiza())) {
                return ResponseEntity.badRequest().body("El usuario que realiza la acción no existe");
            }

            Trazabilidad trazabilidadCreada = trazabilidadDAO.registrar(trazabilidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(trazabilidadCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar trazabilidad: " + e.getMessage());
        }
    }

    /**
     * Consulta el historial de trazabilidad de un proyecto
     * GET /api/trazabilidad/proyecto/{id}
     */
    @GetMapping("/proyecto/{id}")
    public ResponseEntity<List<Trazabilidad>> consultarTrazabilidadPorProyecto(@PathVariable Integer id) {
        try {
            List<Trazabilidad> trazabilidades = trazabilidadDAO.consultarPorProyecto(id);
            return ResponseEntity.ok(trazabilidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

