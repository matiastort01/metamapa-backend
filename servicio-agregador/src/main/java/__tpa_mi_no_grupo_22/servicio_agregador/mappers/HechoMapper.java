package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFrontInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFuenteInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.UbicacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Etiqueta;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class HechoMapper {
    @Autowired
    private FuenteMapper fuenteMapper;

    public HechoOutputDTO hechoToHechoOutputDTO(Hecho hecho) {
      if (hecho == null) return null;

      Ubicacion ubicacion = hecho.getUbicacion();
      String municipio = (ubicacion != null && ubicacion.getMunicipio() != null) ? ubicacion.getMunicipio().getNombre() : null;
      String departamento = (ubicacion != null && ubicacion.getDepartamento() != null) ? ubicacion.getDepartamento().getNombre() : null;
      String provincia = (ubicacion != null && ubicacion.getProvincia() != null) ? ubicacion.getProvincia().getNombre() : null;

      String nombreCategoria = (hecho.getCategoria() != null) ? hecho.getCategoria().getNombre() : "Sin Categoría"; // TODO: ENTIENDO Q ESTO NUNCA DEBERIA SUCEDER

      return HechoOutputDTO.builder()
                .id(hecho.getId())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .fechaHecho(hecho.getFechaHecho())
                .categoria(nombreCategoria)
                .multimedia(hecho.getMultimedia())
                .fuentes(hecho.getFuentes().stream().map(fuente -> fuenteMapper.toFuenteOutputDTO(fuente)).toList())
                .etiquetas(hecho.getEtiquetas().stream().map(Etiqueta::getDescripcion).toList())
                .ubicacionDTO(UbicacionOutputDTO.builder()
                    .municipio(municipio)
                    .departamento(departamento)
                    .provincia(provincia)
                    .latitud(hecho.getUbicacion().getLatitud())
                    .longitud(hecho.getUbicacion().getLongitud())
                    .build()
                )
                .estado(hecho.getEstado().name().toUpperCase())
                .usuario(hecho.getUsuario())
                .build();
    }

    public Hecho hechoFuenteInputDTOToHecho(HechoFuenteInputDTO hechoInput, Fuente fuente, EstadoHecho estadoHecho){  //lo hacemos asi por ahora para que ande pero hay que plantearnos que atributos quereoms enviarnos
      return Hecho.builder()
          .titulo(hechoInput.getTitulo())
          .categoria(new Categoria(hechoInput.getCategoria()))
          .multimedia(hechoInput.getMultimedia())
          .descripcion(hechoInput.getDescripcion())
          .ubicacion(new Ubicacion(hechoInput.getLatitud(),hechoInput.getLongitud()))
          .fechaHecho(hechoInput.getFecha())
          .usuario(hechoInput.getUsuario())
          .fuentes(Set.of(fuente))
          .estado(estadoHecho)
          .build();
    }

  public Hecho hechoFrontInputDTOToHecho(HechoFrontInputDTO hechoInput){
    return Hecho.builder()
        .titulo(hechoInput.getTitulo())
        .descripcion(hechoInput.getDescripcion())
        .categoria(new Categoria(hechoInput.getCategoria()))
        .multimedia(hechoInput.getMultimedia())
        .ubicacion(new Ubicacion(hechoInput.getLatitud(),hechoInput.getLongitud()))
        .fechaHecho(hechoInput.getFecha())
        .usuario(hechoInput.getUsuario())
        .fuentes(new HashSet<>())
        .build();
  }
}
