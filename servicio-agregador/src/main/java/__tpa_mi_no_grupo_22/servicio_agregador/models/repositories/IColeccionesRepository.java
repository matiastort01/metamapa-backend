package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Coleccion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Provincia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IColeccionesRepository extends JpaRepository<Coleccion, Long> {
    @EntityGraph(attributePaths = {"fuentes"})
    @Query("SELECT c FROM Coleccion c")
    List<Coleccion> findAllConFuentes(); // lo uso en el scheduler de consenso para evitar los N+1 selects

    //Optional<Coleccion> findByTitulo(@Param("titulo") String valor);
    Optional<Coleccion> findByTituloIgnoreCase(String titulo);

    List<Coleccion> findTop10ByOrderByFechaCreacionDesc();
}
