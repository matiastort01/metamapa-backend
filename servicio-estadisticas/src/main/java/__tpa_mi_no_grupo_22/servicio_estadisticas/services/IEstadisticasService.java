package __tpa_mi_no_grupo_22.servicio_estadisticas.services;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output.ColeccionOutputDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface IEstadisticasService {
  //public EstadisticaOutputDTO mostrarEstadisticas();


  public Page<CategoriaOutputDTO> obtenerCategorias(List<String> categoria, Boolean top, Pageable pageable);


  public Page<ColeccionOutputDTO> obtenerColecciones(List<String> colecciones,  Pageable pageable);



}
