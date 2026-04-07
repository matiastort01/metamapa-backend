package __tpa_mi_no_grupo_22.servicio_agregador.controllers;


import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.SolicitudModificacionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.services.ISolicitudesEliminacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/metamapa/solicitudes")
public class SolicitudesEliminacionController {
    @Autowired
    private ISolicitudesEliminacionService solicitudesService;

    private static final Logger log = LoggerFactory.getLogger(SolicitudesEliminacionController.class);

    @PostMapping()
    public ResponseEntity<SolicitudEliminacionOutputDTO> crearSolicitud(@RequestBody @Valid SolicitudEliminacionInputDTO solicitudEliminacionInputDTO){
        log.info("usuario en solicitud controller: "+ solicitudEliminacionInputDTO.getUsuario());
        return ResponseEntity.ok(solicitudesService.crearSolicitud(solicitudEliminacionInputDTO));
    }
    //El requestbody mapea el json que recibimos del body al dto.

    // 1. Endpoint para LISTAR pendientes
    // El frontend llama a: GET /metamapa/solicitudes/pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudEliminacionOutputDTO>> obtenerPendientes() {
        // Asumo que agregaste 'obtenerPendientes()' en tu ISolicitudesService
        return ResponseEntity.ok(solicitudesService.obtenerPendientes());
    }

    @GetMapping("{id}")
    public ResponseEntity<SolicitudEliminacionOutputDTO> obtenerSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(this.solicitudesService.obtenerSolicitud(id));
    }

    // 2. Endpoint para ACEPTAR (Eliminar el hecho denunciado)
    // El frontend llama a: POST /metamapa/solicitudes/{id}/aceptar
    @PostMapping("/{id}/aceptar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> aceptarSolicitud(@PathVariable Long id) {
        solicitudesService.aceptarSolicitud(id); // Lógica: borrar hecho y cerrar solicitud
        return ResponseEntity.ok().build();
    }

    // 3. Endpoint para RECHAZAR (Descartar la denuncia)
    // El frontend llama a: POST /metamapa/solicitudes/{id}/rechazar
    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rechazarSolicitud(@PathVariable Long id) {
        solicitudesService.rechazarSolicitud(id); // Lógica: cambiar estado solicitud a RECHAZADA
        return ResponseEntity.ok().build();
    }
}
