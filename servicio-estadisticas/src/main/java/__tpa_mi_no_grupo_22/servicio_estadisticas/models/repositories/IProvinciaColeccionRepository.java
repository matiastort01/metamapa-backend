package __tpa_mi_no_grupo_22.servicio_estadisticas.models.repositories;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.ProvinciaColeccionEstadistica;
import org.springframework.data.domain.Page; // ¡Nueva Importación!
import org.springframework.data.domain.Pageable; // ¡Nueva Importación!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProvinciaColeccionRepository extends JpaRepository<ProvinciaColeccionEstadistica, Long> {

    Optional<ProvinciaColeccionEstadistica> findByTitulo(@Param("titulo") String titulo);

    // 1️⃣ Todas las últimas (una por cada título) - AHORA PAGINADO
    @Query(value = """
            SELECT p.*
            FROM provincia_coleccion p
            INNER JOIN (
                SELECT titulo, MAX(fecha_creacion) AS max_fecha
                FROM provincia_coleccion
                GROUP BY titulo
            ) latest
            ON p.titulo = latest.titulo 
            AND p.fecha_creacion = latest.max_fecha
            """,
            nativeQuery = true)
    Page<ProvinciaColeccionEstadistica> findAllLatest(Pageable pageable); // Cambio: Retorna Page y acepta Pageable


    // 2️⃣ Las últimas filtradas por una lista de títulos - AHORA PAGINADO
    @Query(value = """
            SELECT p.*
            FROM provincia_coleccion p
            INNER JOIN (
                SELECT titulo, MAX(fecha_creacion) AS max_fecha
                FROM provincia_coleccion
                WHERE titulo IN (:titulos)
                GROUP BY titulo
            ) latest
            ON p.titulo = latest.titulo
            AND p.fecha_creacion = latest.max_fecha
            """,
            nativeQuery = true)
    Page<ProvinciaColeccionEstadistica> findLatestByTitulos(@Param("titulos") List<String> titulos, Pageable pageable); // Cambio: Retorna Page y acepta Pageable
}