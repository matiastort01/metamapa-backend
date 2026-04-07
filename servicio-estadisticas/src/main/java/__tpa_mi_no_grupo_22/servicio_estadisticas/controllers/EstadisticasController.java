package __tpa_mi_no_grupo_22.servicio_estadisticas.controllers;


import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output.ColeccionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.CategoriaEstadistica;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.ProvinciaColeccionEstadistica;
import __tpa_mi_no_grupo_22.servicio_estadisticas.services.IAgregadorService;
import __tpa_mi_no_grupo_22.servicio_estadisticas.services.IEstadisticasService;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metamapa/estadisticas")
public class EstadisticasController {

  @Autowired
  private IAgregadorService servicioAgregador;

  @Autowired
  private IEstadisticasService servicioEstadisticas;


    @GetMapping("/categoria")
    public Page<CategoriaOutputDTO> getCategorias( // CAMBIO: Devuelve Page<DTO>
                                                   @RequestParam(required = false) List<String> categorias,
                                                   @RequestParam(required = false) Boolean top,
                                                   // Spring automáticamente mapea los params page, size, sort a un objeto Pageable
                                                   @PageableDefault(size = 10) Pageable pageable) { // CAMBIO: Agregar Pageable

        return servicioEstadisticas.obtenerCategorias(categorias, top, pageable);
    }


    @GetMapping("/colecciones")
    public Page<ColeccionOutputDTO> getColecciones(
            @RequestParam(required = false) List<String> colecciones,
            @PageableDefault(size = 10) Pageable pageable) {

        return servicioEstadisticas.obtenerColecciones(colecciones,pageable);
    }

  // poner scheduler
  @GetMapping
  public void recalcularEstadisticas() {
    servicioAgregador.recalcular();
  }
//TODO
  //¿Cuántas solicitudes de eliminación son spam?
  //De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
}
