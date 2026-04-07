package __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.input;

import java.nio.file.Path;
import java.time.LocalDateTime;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class DisasterInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Path multimedia;
  private Double latitud;
  private Double longitud;

  @JsonProperty("fecha_hecho")
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
      timezone = "UTC")
  private LocalDateTime fechaHecho;

  @JsonProperty("created_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
      timezone = "UTC")
  private LocalDateTime createdAt;

  @JsonProperty("updated_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
      timezone = "UTC")
  private LocalDateTime updatedAt;
}



