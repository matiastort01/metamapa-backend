package __tpa_mi_no_grupo_22.fuente_dinamica.services.solicitudService;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.input.SoliElimInputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.dtos.output.SoliElimOutputDTO;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.hechoRepository.IHechoRepository;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.soliEliminacionRepository.ISoliEliminacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SolicitudService implements ISolicitudService{
  @Autowired
  private ISoliEliminacionRepository soliEliminacionRepository;
  @Autowired
  private IHechoRepository hechoRepository;

  @Override
  public void save(SoliElimInputDTO soliElimInputDTO) throws Exception {
    soliEliminacionRepository.save(solicitudEliminacion(soliElimInputDTO));
  }

  @Override
  public void delete(Long id) {
    soliEliminacionRepository.deleteById(id);
  }

  @Override
  public SoliElimOutputDTO getById(Long id) {
    SolicitudEliminacion soli = soliEliminacionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No existe la Solicitud con id " + id));
    return soliElimOutputDTO(soli);
  }

  @Override
  public List<SoliElimOutputDTO> getSolicitudes() {
    List<SolicitudEliminacion> solicitudes = this.soliEliminacionRepository.findAll();
    return solicitudes.stream().map(this::soliElimOutputDTO).toList();
  }

  // MAPPERS

  private SoliElimOutputDTO soliElimOutputDTO(SolicitudEliminacion soli){
    return new SoliElimOutputDTO(
        soli.getHecho().getTitulo(),
        soli.getJustificacion(),
        soli.getEstadoSolicitud().toString(),
        soli.getFechaCreacion()
    );
  }

  private SolicitudEliminacion solicitudEliminacion(SoliElimInputDTO soli) {
    Long id = soli.getIdHecho();

    Hecho hecho = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No existe el hecho con id " + id));

    return new SolicitudEliminacion(hecho, soli.getJustificacion());
  }
}
