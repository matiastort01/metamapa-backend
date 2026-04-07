package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.ColeccionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.CriterioPertenenciaInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ColeccionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Coleccion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaCategoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaFecha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class ColeccionMapper {
  @Autowired
  private CriterioPertenenciaMapper criterioPertenenciaMapper;
  @Autowired
  private FuenteMapper fuenteMapper;

  public ColeccionOutputDTO toColeccionOutputDTO(Coleccion coleccion) {
    return ColeccionOutputDTO.builder()
        .id(coleccion.getId())
        .titulo(coleccion.getTitulo())
        .descripcion(coleccion.getDescripcion())
        // mapeo criterios -> DTO
        .criteriosDePertenencias(
            coleccion.getCriterios() == null ? List.of() : coleccion.getCriterios()
                .stream()
                .map(criterioPertenenciaMapper::toCriterioPertenenciaOutputDTO)
                .toList()
        )
        // fuentes viene tal cual de la entidad
        .fuentes(coleccion.getFuentes().stream().map(fuente -> fuenteMapper.toFuenteOutputDTO(fuente)).toList())
        // enum en la entidad -> enum en el DTO
        .algoritmoConsenso(coleccion.getTipoAlgoritmoConsenso())
        .build();
  }

  public Coleccion toColeccion(ColeccionInputDTO coleccionInputDTO) {
    if (coleccionInputDTO == null) return null;

    Coleccion coleccion = new Coleccion();
    coleccion.setTitulo(coleccionInputDTO.getTitulo());
    coleccion.setDescripcion(coleccionInputDTO.getDescripcion());
    coleccion.setTipoAlgoritmoConsenso(coleccionInputDTO.getAlgoritmoConsenso());
    // Inicializo vacio el set de fuentes y el list de criterios (el service se encarga de poblarlos)
    coleccion.setFuentes(new HashSet<>());
    coleccion.setCriterios(new ArrayList<>());

    return coleccion;
  }
}
