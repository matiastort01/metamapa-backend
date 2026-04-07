package __tpa_mi_no_grupo_22.fuente_estatica.models.repositories;

import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Long> {
  Optional<Ubicacion> findByLatitudAndLongitud(Double latitud, Double longitud);
}
