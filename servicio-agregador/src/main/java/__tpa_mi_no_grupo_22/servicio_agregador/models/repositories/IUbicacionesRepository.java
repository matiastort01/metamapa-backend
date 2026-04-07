package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface IUbicacionesRepository extends JpaRepository<Ubicacion, Long> {
  public Optional<Ubicacion> findByLatitudAndLongitud(BigDecimal latitud, BigDecimal longitud);
}
