package __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HechoOutputDTO {
  private String titulo;
  private String descripcion;
  private String categoria;
  private List<String> multimedia;
  private BigDecimal latitud;
  private BigDecimal longitud;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime fecha; // fechaHecho
//  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//  private LocalDateTime fechaCarga;
  private String fuenteId;
  private String usuario;
}

