package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.ColeccionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.CriterioPertenenciaInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ColeccionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.PageOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.ModoNavegacion;

import java.util.List;

public interface IColeccionesService {
  ColeccionOutputDTO obtenerColeccion(Long id);

  List<ColeccionOutputDTO> obtenerColecciones(List<Long> ids);

  List<HechoOutputDTO> obtenerHechosAsociadosColeccion(Long id, FiltrosHechoInputDTO filtros, ModoNavegacion modoNavegacion);

  PageOutputDTO<HechoOutputDTO> obtenerHechosAsociadosColeccionPaginado(Long id, FiltrosHechoInputDTO filtros, ModoNavegacion modo, Integer page, Integer size);

  ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO);

  void eliminarColeccion(Long id);

  ColeccionOutputDTO editarColeccion(Long id, ColeccionInputDTO coleccionInput);
}