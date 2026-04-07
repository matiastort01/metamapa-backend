package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.EstadoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IEstadoConsensoRepository extends JpaRepository<EstadoConsenso, Long> {
  Optional<EstadoConsenso> findByTituloHechoAndTituloColeccionAndTipoAlgoritmoConsenso(String tituloHecho, String tituloColeccion, TipoAlgoritmoConsenso tipoAlgoritmoConsenso);

  @Query("SELECT e.tituloHecho FROM EstadoConsenso e " +
      "WHERE e.tituloColeccion = :tituloColeccion " +
      "AND e.tipoAlgoritmoConsenso = :tipo " +
      "AND e.consensuado = true")
  Set<String> findTitulosConsensuados(
      @Param("tituloColeccion") String tituloColeccion,
      @Param("tipo") TipoAlgoritmoConsenso tipo
  );
}
