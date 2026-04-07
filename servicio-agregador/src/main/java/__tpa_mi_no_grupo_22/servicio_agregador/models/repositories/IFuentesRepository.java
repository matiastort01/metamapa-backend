package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFuentesRepository extends JpaRepository<Fuente, Long> {
  Optional<Fuente> findByNombre(String nombreFuente);
}
