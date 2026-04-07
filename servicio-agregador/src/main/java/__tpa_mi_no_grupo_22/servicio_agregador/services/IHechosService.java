package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFrontInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFuenteInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.PageOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {
  List<HechoOutputDTO> obtenerHechos(FiltrosHechoInputDTO filtros);

  List<Hecho> obtenerHechosDominio(FiltrosHechoInputDTO filtros);

  PageOutputDTO<HechoOutputDTO> obtenerHechosPaginado(FiltrosHechoInputDTO filtros, Integer page, Integer size);

  HechoOutputDTO obtenerHecho(Long id);

  Hecho obtenerHechoDominio(Long id);

  HechoOutputDTO obtenerUltimo();

  Mono<Void> actualizarHechosFuente(TipoFuente tipoFuente);

  Mono<Hecho> guardarHechoNormalizado(Hecho hecho);

  HechoOutputDTO editarHecho(Long id, HechoFrontInputDTO hechoInputDTO);

  Void aprobarHecho(Long id);

  Void rechazarHecho(Long id);

  List<CategoriaOutputDTO> obtenerCategorias();

}
