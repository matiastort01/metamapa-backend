package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input;


import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosHechoInputDTO {
  private List<Long> categorias;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // TODO: Ver si con valid puedo manejar los valores de los filtros para poder devolver excepciones
  private LocalDate fechaReporteDesde;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaReporteHasta;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaAcontecimientoDesde;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaAcontecimientoHasta;
  private BigDecimal latitud; // TODO: Ver si se puede hacer con provincia, municipio y departamento en lugar de lat y lon
  private BigDecimal longitud;
  private List<Long> fuentes;
  private String usuario;
  private Boolean activo;
  private EstadoHecho estado;
}
