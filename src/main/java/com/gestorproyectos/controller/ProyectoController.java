package com.gestorproyectos.controller;

import com.gestorproyectos.dao.ProyectoDAO;
import com.gestorproyectos.model.Proyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de proyectos
 */
@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {

    @Autowired
    private ProyectoDAO proyectoDAO;

    /**
     * Lista todos los proyectos
     * GET /api/proyectos
     */
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        try {
            List<Proyecto> proyectos = proyectoDAO.listar();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crea un nuevo proyecto
     * POST /api/proyectos
     */
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody Proyecto proyecto) {
        try {
            // Validaciones básicas
            if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del proyecto es obligatorio");
            }
            if (proyecto.getDescripcion() == null || proyecto.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La descripción del proyecto es obligatoria");
            }
            if (proyecto.getIdGestorAsignado() == null) {
                return ResponseEntity.badRequest().body("El gestor asignado es obligatorio");
            }

            Proyecto proyectoCreado = proyectoDAO.crear(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(proyectoCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear proyecto: " + e.getMessage());
        }
    }

    /**
     * Busca un proyecto por su ID
     * GET /api/proyectos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> buscarProyectoPorId(@PathVariable Integer id) {
        try {
            Proyecto proyecto = proyectoDAO.buscarPorId(id);
            if (proyecto != null) {
                return ResponseEntity.ok(proyecto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

