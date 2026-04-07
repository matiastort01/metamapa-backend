package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActividadDTO {
  private String tipo;        // Ej: "HECHO_CREADO", "SOLICITUD_BAJA", "COLECCION_NUEVA"
  private String descripcion; // Ej: "Se creó el hecho: 'Tormenta...'"
  private String autor;       // Ej: "admin@metamapa.com" o "ANONIMO"

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  private LocalDateTime fecha;
}
