package __tpa_mi_no_grupo_22.servicio_agregador.controllers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ActividadDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.DashboardSummaryDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IAdminService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.impl.FuenteEstatica;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/metamapa/admin")
@RequiredArgsConstructor
public class AdminController {

  private final IAdminService adminService;

  @GetMapping("/summary")
  public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
    return ResponseEntity.ok(adminService.getSummary());
  }

  @GetMapping("/actividad-reciente")
  public ResponseEntity<List<ActividadDTO>> getActividadReciente() {
    return ResponseEntity.ok(adminService.obtenerActividadReciente());
  }
}
