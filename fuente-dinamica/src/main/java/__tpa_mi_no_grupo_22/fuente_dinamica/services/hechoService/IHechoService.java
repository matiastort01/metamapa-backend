package __tpa_mi_no_grupo_22.fuente_dinamica.services.hechoService;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.HechoOutputDTO;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public interface IHechoService {
  List<HechoOutputDTO> getAll(String categoria, Double latitud, Double longitud, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta);

  HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO);

//  public HechoOutputDTO getHechoById(Long id);
//
//  public void delete(Long id);
//
//  public HechoOutputDTO guardarOActualizar(HechoInputDTO input);
}
