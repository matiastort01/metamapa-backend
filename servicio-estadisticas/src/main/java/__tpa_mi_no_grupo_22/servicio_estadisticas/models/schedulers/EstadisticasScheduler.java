package __tpa_mi_no_grupo_22.servicio_estadisticas.models.schedulers;


import __tpa_mi_no_grupo_22.servicio_estadisticas.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EstadisticasScheduler {
    @Autowired
    private IAgregadorService agregadorService;


    @Scheduled(cron = "0 0 4 * * *")
    public void recalcular(){
        System.out.println("Entre al crono");
        agregadorService.recalcular();
    }
}
