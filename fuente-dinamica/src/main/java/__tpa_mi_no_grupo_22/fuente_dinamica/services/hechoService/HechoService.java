package __tpa_mi_no_grupo_22.fuente_dinamica.services.hechoService;

import __tpa_mi_no_grupo_22.fuente_dinamica.exceptions.ValidationBusinessException;
import __tpa_mi_no_grupo_22.fuente_dinamica.mappers.HechoMapper;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Ubicacion;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.ICategoriaRepository;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.IUbicacionRepository;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.hechoRepository.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
@Service
public class HechoService implements IHechoService{
  @Autowired
  private IHechoRepository hechoRepository;
  @Autowired
  private ICategoriaRepository categoriaRepository;
  @Autowired
  private IUbicacionRepository ubicacionRepository;
  @Autowired
  private HechoMapper hechoMapper;

  @Override
  public List<HechoOutputDTO> getAll(
      String categoria,
      Double latitud,
      Double longitud,
      LocalDate fechaReporteDesde,
      LocalDate fechaReporteHasta,
      LocalDate fechaAcontecimientoDesde,
      LocalDate fechaAcontecimientoHasta
  ) {
    return hechoRepository.findAll()
        .stream()
        .filter(Hecho::getActivo)
        .filter(hecho -> categoria == null || hecho.getCategoria().getNombre().equalsIgnoreCase(categoria))
        .filter(hecho -> latitud == null || hecho.getUbicacion().getLatitud().equals(latitud))
        .filter(hecho -> longitud == null || hecho.getUbicacion().getLongitud().equals(longitud))
        .filter(hecho -> fechaReporteDesde == null || !hecho.getFechaDeCarga().toLocalDate().isBefore(fechaReporteDesde))
        .filter(hecho -> fechaReporteHasta == null || !hecho.getFechaDeCarga().toLocalDate().isAfter(fechaReporteHasta))
        .filter(hecho -> fechaAcontecimientoDesde == null || !hecho.getFechaHecho().toLocalDate().isBefore(fechaAcontecimientoDesde))
        .filter(hecho -> fechaAcontecimientoHasta == null || !hecho.getFechaHecho().toLocalDate().isAfter(fechaAcontecimientoHasta))
        .map(hechoMapper::toHechoOutputDTO)
        .toList();
  }

  @Override
  @Transactional
  public HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO){
    Map<String, String> errores = new HashMap<>();

    // Título Único
    if (this.hechoRepository.findByTituloIgnoreCase(hechoInputDTO.getTitulo()).isPresent()) {
      errores.put("titulo", "Ya existe un hecho con el título: " + hechoInputDTO.getTitulo());
    }

    if (!errores.isEmpty()) {
      // Lanza la excepción que se mapeará a HTTP 422 y que contiene todos los errores de campo acumulados
      throw new ValidationBusinessException(errores);
    }

    Hecho hecho = this.hechoMapper.toHecho(hechoInputDTO);

    hecho.setFechaDeCarga(LocalDateTime.now());
    hecho.setActivo(true);
    hecho.setUsuario(hechoInputDTO.getUsuario() != null ? hechoInputDTO.getUsuario() : "ANONIMO");

    Optional<Categoria> catOpt = this.categoriaRepository.findByNombre(hecho.getCategoria().getNombre());
    if (catOpt.isPresent()) {
      hecho.setCategoria(catOpt.get());
    } else {
      hecho.setCategoria(this.categoriaRepository.save(hecho.getCategoria()));
    }

    Optional<Ubicacion> ubiOpt = this.ubicacionRepository.findByLatitudAndLongitud(hecho.getUbicacion().getLatitud(), hecho.getUbicacion().getLongitud());
    if (ubiOpt.isPresent()) {
      hecho.setUbicacion(ubiOpt.get());
    } else {
      hecho.setUbicacion(this.ubicacionRepository.save(hecho.getUbicacion()));
    }

    this.hechoRepository.save(hecho);

    return this.hechoMapper.toHechoOutputDTO(hecho);
  }

//  @Override
//  public void delete(Long id) {
//    hechoRepository.deleteById(id);
//  }
//
//  @Override
//  public HechoOutputDTO getHechoById(Long id) {
//    Hecho hecho = hechoRepository.findById(id)
//        .orElseThrow(() -> new RuntimeException("No existe el Hecho con id " + id));
//    return hechoOutputDTO(hecho);
//  }
}