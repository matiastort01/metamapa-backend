package __tpa_mi_no_grupo_22.servicio_agregador.controllers;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.ColeccionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ColeccionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.PageOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.ModoNavegacion;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IColeccionesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/metamapa/colecciones")
@Validated
public class ColeccionesController {
    private static final Logger log = LoggerFactory.getLogger(ColeccionesController.class);

    @Autowired
    private IColeccionesService coleccionesService;

    @GetMapping
    public ResponseEntity<List<ColeccionOutputDTO>> obtenerColecciones(
        @RequestParam(required = false) List<Long> ids
    ){
        return ResponseEntity.ok(this.coleccionesService.obtenerColecciones(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColeccionOutputDTO> obtenerColeccion(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.coleccionesService.obtenerColeccion(id));
    }

    @GetMapping("/{identificador}/hechos")
    public Mono<ResponseEntity<PageOutputDTO<HechoOutputDTO>>> obtenerHechosDeLaColeccionPaginado(
        @PathVariable("identificador") Long id,
        @RequestParam(required = false) List<Long> categorias,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
        @RequestParam(required = false) BigDecimal latitud,
        @RequestParam(required = false) BigDecimal longitud,
        @RequestParam(required = false) List<Long> fuentes,
        @RequestParam(required = false) String usuario,
        @RequestParam(required = false, defaultValue = "CURADA") ModoNavegacion modoNavegacion,
        @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
        @RequestParam(required = false, defaultValue = "20") @Min(value = 1, message = "El tamaño debe ser al menos 1") @Max(value = 100, message = "No puedes pedir más de 100 hechos por vez") Integer size
    ) {
        // Creo los filtros
        FiltrosHechoInputDTO filtros = FiltrosHechoInputDTO.builder()
            .categorias(categorias)
            .fechaReporteDesde(fechaReporteDesde)
            .fechaReporteHasta(fechaReporteHasta)
            .fechaAcontecimientoDesde(fechaAcontecimientoDesde)
            .fechaAcontecimientoHasta(fechaAcontecimientoHasta)
            .latitud(latitud)
            .longitud(longitud)
            .fuentes(fuentes)
            .usuario(usuario)
            .build();

        return Mono.fromCallable(() -> ResponseEntity.ok(this.coleccionesService.obtenerHechosAsociadosColeccionPaginado(id, filtros, modoNavegacion, page, size)))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/estadistica/{identificador}/hechos")
    public Mono<ResponseEntity<List<HechoOutputDTO>>> obtenerHechosDeLaColeccion(
        @PathVariable("identificador") Long id,
        @RequestParam(required = false) List<Long> categorias,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
        @RequestParam(required = false) BigDecimal latitud,
        @RequestParam(required = false) BigDecimal longitud,
        @RequestParam(required = false) List<Long> fuentes,
        @RequestParam(required = false) String usuario,
        @RequestParam(required = false, defaultValue = "CURADA") ModoNavegacion modoNavegacion
    ) {
        // Creo los filtros
        FiltrosHechoInputDTO filtros = FiltrosHechoInputDTO.builder()
                .categorias(categorias)
                .fechaReporteDesde(fechaReporteDesde)
                .fechaReporteHasta(fechaReporteHasta)
                .fechaAcontecimientoDesde(fechaAcontecimientoDesde)
                .fechaAcontecimientoHasta(fechaAcontecimientoHasta)
                .latitud(latitud)
                .longitud(longitud)
                .fuentes(fuentes)
                .usuario(usuario)
                .build();

        return Mono.fromCallable(() -> ResponseEntity.ok(this.coleccionesService.obtenerHechosAsociadosColeccion(id, filtros, modoNavegacion)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColeccionOutputDTO> crearColeccion(@RequestBody @Valid ColeccionInputDTO coleccionInputDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.coleccionesService.crearColeccion(coleccionInputDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColeccionOutputDTO> editarColeccion(
        @PathVariable("id") Long id,
        @RequestBody @Valid ColeccionInputDTO coleccionInputDTO
    ) {
        return ResponseEntity.ok(this.coleccionesService.editarColeccion(id, coleccionInputDTO));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarColeccion(@RequestParam Long id){
        coleccionesService.eliminarColeccion(id);
        return ResponseEntity.noContent().build();
    }
}
