package __tpa_mi_no_grupo_22.servicio_estadisticas.models.repositories;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.CategoriaEstadistica;
import org.springframework.data.domain.Page; // Nueva importación
import org.springframework.data.domain.Pageable; // Nueva importación
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICategoriasRepository extends JpaRepository<CategoriaEstadistica, Long> {

    // TOP 1 por cantidad desc (sin cambios)
    Optional<CategoriaEstadistica> findTopByOrderByCantidadDesc();

    // 1. Método para obtener TODAS las últimas categorías PAGINADAS
    // Devuelve Page en lugar de List, y acepta Pageable
    @Query("""
       SELECT c
       FROM CategoriaEstadistica c
       WHERE c.horaCreacion = (
            SELECT MAX(c2.horaCreacion)
            FROM CategoriaEstadistica c2
            WHERE c2.nombre = c.nombre
       )
       ORDER BY c.nombre
       """)
    Page<CategoriaEstadistica> findUltimas(Pageable pageable); // CAMBIO: Pageable y Page

    // 2. Método para obtener las últimas categorías FILTRADAS PAGINADAS
    // Devuelve Page en lugar de List, y acepta Pageable
    @Query("""
       SELECT c
       FROM CategoriaEstadistica c
       WHERE c.nombre IN :nombres
         AND c.horaCreacion = (
            SELECT MAX(c2.horaCreacion)
            FROM CategoriaEstadistica c2
            WHERE c2.nombre = c.nombre
         )
       ORDER BY c.nombre
       """)
    Page<CategoriaEstadistica> findUltimasFiltradas(List<String> nombres, Pageable pageable); // CAMBIO: Pageable y Page
}