package __tpa_mi_no_grupo_22.servicio_estadisticas.services.impl;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.output.ColeccionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.CategoriaEstadistica;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.Coleccion;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities.ProvinciaColeccionEstadistica;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.repositories.IProvinciaColeccionRepository;
import __tpa_mi_no_grupo_22.servicio_estadisticas.services.IEstadisticasService;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService implements IEstadisticasService {
  @Autowired
  ICategoriasRepository categoriasRepository;

  @Autowired
  IProvinciaColeccionRepository coleccionesRepository;


    @Override
    public Page<CategoriaOutputDTO> obtenerCategorias(List<String> categorias, Boolean top, Pageable pageable) {

        // 1. Si pide top → Retornar solo 1 categoría (Esta lógica no es paginable y debe ser manejada de forma separada si el front la necesita)
        if (Boolean.TRUE.equals(top)) {
            // Opción 1: Simplemente ignoramos la paginación y devolvemos la lista de top 1 envuelta en Page.
            // Esto requiere un manejo especial, ya que tu repositorio devuelve Optional para el top 1.
            List<CategoriaEstadistica> topList = categoriasRepository.findTopByOrderByCantidadDesc()
                    .map(List::of)
                    .orElse(List.of());

            // Creamos un Page manual para mantener la firma del método
            return new PageImpl<>(
                    topList.stream().map(this::categoriaMapperAOutputDTO).toList(),
                    pageable,
                    topList.size() // Total de elementos es solo 1 (o 0)
            );
        }

        Page<CategoriaEstadistica> entidadPage;

        // 2. Si NO hay categorías → devolver todas PAGINADAS
        if (categorias == null || categorias.isEmpty()) {
            entidadPage = categoriasRepository.findUltimas(pageable);
        } else {
            // 3. Si hay categorías → devolver filtradas PAGINADAS
            entidadPage = categoriasRepository.findUltimasFiltradas(categorias, pageable);
        }

        // 4. Mapear Page<Entity> a Page<DTO>
        // El método .map() de Page se encarga de aplicar el mapeo a cada elemento
        // y conservar todos los metadatos de paginación (totalElements, totalPages, etc.)
        return entidadPage.map(this::categoriaMapperAOutputDTO);
    }



    @Override
    public Page<ColeccionOutputDTO> obtenerColecciones(List<String> colecciones, Pageable pageable) {

        Page<ProvinciaColeccionEstadistica> entidadPage;

        // 1. Si NO hay colecciones → devolver todas PAGINADAS
        if (colecciones == null || colecciones.isEmpty()) {
            entidadPage = coleccionesRepository.findAllLatest(pageable);
        } else {

            entidadPage = coleccionesRepository.findLatestByTitulos(colecciones, pageable);
        }

        return entidadPage.map(this::coleccionMapperAOutputDTO);
    }


  private CategoriaOutputDTO categoriaMapperAOutputDTO(CategoriaEstadistica c){
     return CategoriaOutputDTO.builder()
             .categoria(c.getNombre())
             .cantidad(c.getCantidad())
             .provincia(c.getProvincia())
             .hora(c.getHora())
             .fecha(c.getHoraCreacion())
             .build();
  }

  private ColeccionOutputDTO coleccionMapperAOutputDTO(ProvinciaColeccionEstadistica p){
        return ColeccionOutputDTO.builder()
                .titulo(p.getTitulo())
                .provincia(p.getProvincia())
                .fechaCreacion(p.getFechaCreacion())
                .cantidad(p.getCantidad())
                .build();
  }
}
