package __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UbicacionOutputDTO {
    private String provincia;
    private String municipio;
    private String departamento;
    private Double latitud;
    private Double longitud;
}
