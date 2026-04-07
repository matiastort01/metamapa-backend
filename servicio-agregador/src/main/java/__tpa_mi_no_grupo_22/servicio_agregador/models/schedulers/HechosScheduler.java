package __tpa_mi_no_grupo_22.servicio_agregador.models.schedulers;

import __tpa_mi_no_grupo_22.servicio_agregador.services.TipoFuente;
import __tpa_mi_no_grupo_22.servicio_agregador.services.impl.HechosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HechosScheduler {

  private HechosService hechosService;

  public HechosScheduler(HechosService hechosService) {
    this.hechosService = hechosService;
  }

  public Void refresh() {
    System.out.println("Entre al crono");
    this.hechosService.actualizarHechosFuente(TipoFuente.NO_PROXY).subscribe();
    return null;
  }
}
