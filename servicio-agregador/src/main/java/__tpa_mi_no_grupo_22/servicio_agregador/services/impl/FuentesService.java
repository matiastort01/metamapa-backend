package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ResourceNotFoundException;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.FuenteMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.FuenteOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IFuentesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuentesService implements IFuentesService {
  @Autowired
  private IFuentesRepository fuentesRepository;
  @Autowired
  private FuenteMapper fuenteMapper;

  @Override
  public List<FuenteOutputDTO> getFuentes() {
    List<Fuente> listaFuentes = fuentesRepository.findAll();

    return listaFuentes.stream().map(fuente -> fuenteMapper.toFuenteOutputDTO(fuente)).toList();
  }

  public Fuente obtenerFuenteDominio(Long id) {
    return fuentesRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No se encontró la fuente con id: " + id));
  }
}
