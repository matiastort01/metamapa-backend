package __tpa_mi_no_grupo_22.fuente_estatica.models.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Data
public class HechoOutputDTO {

  private String titulo;

  private String descripcion;

  private String categoria;

  private Path multimedia;

  private Double latitud;

  private Double longitud;

  private LocalDate fecha;

  private LocalDateTime fechaCarga;

  private String fuenteId;

  private String estado;

  private String usuario;

  public HechoOutputDTO(String titulo, String descripcion, String categoria, Path multimedia, Double latitud, Double longitud, LocalDate fecha, LocalDateTime fechaCarga, String fuenteId) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.multimedia = multimedia;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fecha = fecha;
    this.fechaCarga = fechaCarga;
    this.fuenteId = fuenteId;
    this.estado = "APROBADO";
    this.usuario = "archivoCSV"; //comentario
  }
}
