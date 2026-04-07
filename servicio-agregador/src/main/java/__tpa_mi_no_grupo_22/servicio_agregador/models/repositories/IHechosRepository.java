package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IHechosRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
  @EntityGraph(attributePaths = {"fuentes", "multimedia"})
  @Query("SELECT h FROM Hecho h WHERE h.id = :id")
  Optional<Hecho> findByIdConFuentesYMultimedia(@Param("id") Long id);

  @EntityGraph(attributePaths = {"fuentes","multimedia"}) // Para solucionar el problema de LazyInitialization
  Optional<Hecho> findByTitulo(String titulo);

  // Metodo usado para hidratar los hechos
  @EntityGraph(attributePaths = {"fuentes", "multimedia"})
  List<Hecho> findByIdIn(List<Long> ids);

  Long countByEstado(EstadoHecho estado);

  List<Hecho> findAllByActivoIsTrueAndEstado(EstadoHecho estado);

  // @EntityGraph hace un LEFT JOIN con 'fuentes' para traerlas en la misma query.
  // Evita el problema "N+1 Selects" y el error de LazyInitialization en el CONSENSO SCHEDULER
  // error de LazyInitialization: En criollo el problema es q al haber una relacion many to many con fuentes, como esta configurado como lazy no me trae las fuentes cuando pido los hechos, entonces cuando las necesito la conexion con la bd ya se cerro y rompe
  // N+1: Se realiza un select para traer los hechos. Como dsp me meto en un bucle for y hago getFuentes, como al ser lazy tiene q ir devuelta a la bd hace otro select solo para las fuentes, entonces hace N (fuentes) + 1 selects. En cambio, si pido todo de una con el left join se hace una unica consulta a la bd
  @EntityGraph(attributePaths = {"fuentes"})
  List<Hecho> findDistinctByActivoTrueAndEstadoAndFuentes_IdIn(EstadoHecho estado, Collection<Long> idsFuentes);

  Optional<Hecho> findTopByOrderByIdDesc();

  List<Hecho> findTop10ByOrderByFechaDeCargaDesc();

  Boolean existsByTituloAndIdNot(String titulo, Long id);
}