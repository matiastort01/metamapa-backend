package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ActividadDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.DashboardSummaryDTO;
import java.util.List;

public interface IAdminService {
  DashboardSummaryDTO getSummary();

  public List<ActividadDTO> obtenerActividadReciente();
}