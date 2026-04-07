package __tpa_mi_no_grupo_22.fuente_dinamica.services.solicitudService;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.SoliElimInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.SoliElimOutputDTO;

import java.util.List;

public interface ISolicitudService {
  public void save(SoliElimInputDTO soliElimInputDTO) throws Exception;

  public void delete(Long id);

  public SoliElimOutputDTO getById(Long Id);

  public List<SoliElimOutputDTO> getSolicitudes();
}
