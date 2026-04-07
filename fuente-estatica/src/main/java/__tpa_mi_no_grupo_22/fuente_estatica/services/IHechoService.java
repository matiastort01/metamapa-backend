package __tpa_mi_no_grupo_22.fuente_estatica.services;

import __tpa_mi_no_grupo_22.fuente_estatica.models.dtos.HechoOutputDTO;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public interface IHechoService {

  public List<HechoOutputDTO> getAll(String categoria, Double latitud, Double longitud, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta);

  public HechoOutputDTO getHechoById(Long id);

  public void bringIn(Path csvFile) throws Exception;

  public void delete(Long id);
}
