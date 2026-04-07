package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO para transportar los datos de resumen para el Dashboard del Administrador.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO implements Serializable {
  // El número total de hechos que están pendientes de revisión.
  private long hechosPendientes;

  //El número total de solicitudes de eliminación que están pendientes.
  private long solicitudesEliminacion;

  //El número total de solicitudes de modificación que están pendientes.
  private long solicitudesModificacion;

  //El número total de colecciones activas en el sistema.
  private long coleccionesActivas;
}
