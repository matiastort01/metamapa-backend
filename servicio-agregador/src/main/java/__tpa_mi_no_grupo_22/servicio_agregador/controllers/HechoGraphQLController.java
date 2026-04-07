package __tpa_mi_no_grupo_22.servicio_agregador.controllers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.HechoMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class HechoGraphQLController {

  @Autowired
  private IHechosService hechosService;

  @Autowired
  private HechoMapper hechoMapper;

  @QueryMapping
  public List<HechoOutputDTO> buscarHechos(
      @Argument Long categoria,
      @Argument Long fuente,
      @Argument String usuario,
      @Argument Integer page,
      @Argument Integer size
  ) {
    // 1. Construir el DTO que tu servicio YA sabe usar
    FiltrosHechoInputDTO filtros = new FiltrosHechoInputDTO();

    // Tu servicio espera List<Long> para categorías y fuentes, así que los convertimos
    if (categoria != null) {
      filtros.setCategorias(List.of(categoria));
    }
    if (fuente != null) {
      filtros.setFuentes(List.of(fuente));
    }

    // Asignamos el resto
    filtros.setUsuario(usuario);

    // 2. REUTILIZAR tu lógica existente
    return hechosService.obtenerHechos(filtros);
  }
}