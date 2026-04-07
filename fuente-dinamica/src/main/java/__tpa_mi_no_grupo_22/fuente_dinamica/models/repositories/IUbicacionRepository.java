package __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Long> {
  Optional<Ubicacion> findByLatitudAndLongitud(BigDecimal latitud, BigDecimal longitud);
}
