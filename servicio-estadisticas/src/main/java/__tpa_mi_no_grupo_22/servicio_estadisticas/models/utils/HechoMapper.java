package __tpa_mi_no_grupo_22.servicio_estadisticas.models.utils;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.Hecho;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {

  //    public HechoOutputDTO hechoToHechoOutputDTO(Hecho hecho) {
//        return HechoOutputDTO.builder()
//                .titulo(hecho.getTitulo())
//                .fechaHecho(hecho.getFechaHecho())
//                .categoria(hecho.getCategoria().getNombre())
//                .multimedia(hecho.getMultimedia())
//                .descripcion(hecho.getDescripcion())
//                .etiquetas(hecho.getEtiquetas().stream().map(Etiqueta::getDescripcion).toList())
//                .latitud(hecho.getUbicacion().getLat())
//                .longitud(hecho.getUbicacion().getLon())
//                .build();
//    }
  public Hecho hechoInputDTOToHecho(HechoInputDTO hechoInput) {  //lo hacemos asi por ahora para que ande pero hay que plantearnos que atributos quereoms enviarnos
    return Hecho.builder()
        .titulo(hechoInput.getTitulo())
        .categoria(hechoInput.getCategoria())
        .provincia(
            hechoInput.getUbicacionDTO()!= null
                ? hechoInput.getUbicacionDTO().getProvincia()
                : null
        )
        .fechaHecho(
            hechoInput.getFechaHecho() != null
                ? hechoInput.getFechaHecho()
                : null
        )
        .build();
  }
}
/*
* private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private List<String> multimedia;
  private List<FuenteOutputDTO> fuentes;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime fechaHecho;
  private UbicacionOutputDTO ubicacionDTO;
  private List<String> etiquetas;
  private String estado;
  private String usuario;
  * */