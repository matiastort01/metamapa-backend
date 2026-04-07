package __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Coleccion {
    private Long id;
    private String titulo;
    private String descripcion;
}
