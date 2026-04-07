package __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<String> multimedia;
    private List<FuenteInputDTO> fuentes;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHecho;
    private UbicacionOutputDTO ubicacionDTO;
    private List<String> etiquetas;
    private String estado;
    private String usuario;
}
