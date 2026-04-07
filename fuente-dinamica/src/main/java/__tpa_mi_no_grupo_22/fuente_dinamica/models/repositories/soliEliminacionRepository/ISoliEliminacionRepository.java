package __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.soliEliminacionRepository;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ISoliEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
}
