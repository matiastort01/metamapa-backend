package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.FuenteOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;

import java.util.List;

public interface IFuentesService {
  List<FuenteOutputDTO> getFuentes();
  Fuente obtenerFuenteDominio(Long id);
}
