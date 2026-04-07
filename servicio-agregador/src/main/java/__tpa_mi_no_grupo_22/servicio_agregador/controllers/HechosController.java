package __tpa_mi_no_grupo_22.servicio_agregador.controllers;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.InternalServerErrorException;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFrontInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.RequestToken;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.PageOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.HechoMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.schedulers.HechosScheduler;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IHechosService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.ISeederService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.TipoFuente;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IServicioGeolocalizacion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/metamapa/hechos")
@Validated
public class HechosController {
    @Autowired
    private IHechosService hechosService ;
    @Autowired
    private ISeederService seeder;
    @Autowired
    private IServicioGeolocalizacion servicioGeolocalizacion;
    @Autowired
    private HechoMapper hechoMapper;
    private static final Logger log = LoggerFactory.getLogger(HechosController.class);

    @Autowired
    private HechosScheduler hechosScheduler;

    @GetMapping("/consenso")
    public ResponseEntity<Void> iniciarConsenso() {
        return ResponseEntity.ok(this.hechosScheduler.refresh());
    }

    @GetMapping
    public Mono<ResponseEntity<List<HechoOutputDTO>>> obtenerHechos(
        @RequestParam(required = false) List<Long> categorias,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
        @RequestParam(required = false) BigDecimal latitud,
        @RequestParam(required = false) BigDecimal longitud,
        @RequestParam(required = false) List<Long> fuentes,
        @RequestParam(required = false) String usuario
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

        return Mono.fromCallable(() -> ResponseEntity.ok(hechosService.obtenerHechos(filtros)))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(value = "/paginado")
    public Mono<ResponseEntity<PageOutputDTO<HechoOutputDTO>>> obtenerHechosPaginados(
        @RequestParam(required = false) List<Long> categorias,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
        @RequestParam(required = false) BigDecimal latitud,
        @RequestParam(required = false) BigDecimal longitud,
        @RequestParam(required = false) List<Long> fuentes,
        @RequestParam(required = false) String usuario,
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
            .activo(true)
            .estado(EstadoHecho.ACEPTADO)
            .build();

        return Mono.fromCallable(() -> ResponseEntity.ok(this.hechosService.obtenerHechosPaginado(filtros, page, size)))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HechoOutputDTO> obtenerHecho(@PathVariable("id") Long id){
        return ResponseEntity.ok(hechosService.obtenerHecho(id));
    }

    // TODO: ARMAR UN ENDPOINT PAGINADO
    @GetMapping("/pendientes")
    public Mono<ResponseEntity<PageOutputDTO<HechoOutputDTO>>> obtenerHechosPendientes(
        @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
        @RequestParam(required = false, defaultValue = "20") @Min(value = 1, message = "El tamaño debe ser al menos 1") @Max(value = 100, message = "No puedes pedir más de 100 hechos por vez") Integer size
    ) {
        FiltrosHechoInputDTO filtros = FiltrosHechoInputDTO.builder()
            .activo(true)
            .estado(EstadoHecho.PENDIENTE)
            .build();

        return Mono.fromCallable(() -> ResponseEntity.ok(this.hechosService.obtenerHechosPaginado(filtros, page, size)))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/mis-hechos")
    public Mono<ResponseEntity<PageOutputDTO<HechoOutputDTO>>> obtenerMisHechos(
        @RequestParam(required = true) String usuario, // TODO: DEBERIA VENIR EN EL AUTHENTICATION
        @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
        @RequestParam(required = false, defaultValue = "20") @Min(value = 1, message = "El tamaño debe ser al menos 1") @Max(value = 100, message = "No puedes pedir más de 100 hechos por vez") Integer size
    ) {
        // Creo los filtros
        FiltrosHechoInputDTO filtros = FiltrosHechoInputDTO.builder()
            .usuario(usuario)
            .activo(null)
            .estado(null) // Lo seteo en null para que traiga los hechos de cualquier estado (ACEPTADO, PENDIENTE y RECHAZADO)
            .build();

        return Mono.fromCallable(() -> ResponseEntity.ok(this.hechosService.obtenerHechosPaginado(filtros, page, size)))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/ultimo")
    public ResponseEntity<HechoOutputDTO> obtenerUltimoHecho() {
        return ResponseEntity.ok(hechosService.obtenerUltimo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HechoOutputDTO> editarHecho(
        @PathVariable("id") Long id,
        @RequestBody @Valid HechoFrontInputDTO hechoInputDTO
    ){
        return ResponseEntity.ok(hechosService.editarHecho(id, hechoInputDTO));
    }

    // FORMA CON TOKEN EN HEADER
//    @PostMapping("/{id}/editar")
//    public Mono<ResponseEntity<HechoOutputDTO>> editarHecho(
//        @PathVariable("id") Long id,
//        @RequestBody @Valid HechoFrontInputDTO hechoInputDTO,
//        @RequestHeader(HttpHeaders.AUTHORIZATION) String token // Si usas headers
//    ) {
//        return Mono.fromCallable(() -> JwtUtil.validarToken(token)) // Validar token
//            .subscribeOn(Schedulers.boundedElastic())
//            .flatMap(usuario -> {
//                // Opcional: Validar que el usuario sea dueño del hecho si es necesario
//                // Podria sumarse la validacion de los 7 dias
//                return hechosService.editarHecho(id, hechoInputDTO);
//            })
//            .map(ResponseEntity::ok);
//    }

    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> aprobarHecho(@PathVariable("id") Long id){
        return ResponseEntity.ok(hechosService.aprobarHecho(id));
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rechazarHecho(@PathVariable("id") Long id){
        return ResponseEntity.ok(hechosService.rechazarHecho(id));
    }

    @GetMapping("/actualizar")
    public ResponseEntity<Mono<Boolean>> actualizarHechos() {
        return ResponseEntity.ok(hechosService.actualizarHechosFuente(TipoFuente.NO_PROXY)
            .thenReturn(true)); // Espera a que termine y luego retorna true
    }

    @GetMapping("/actualizar/proxy")
    public Mono<ResponseEntity<Void>> actualizarDesdeProxy() {
        return this.hechosService.actualizarHechosFuente(TipoFuente.PROXY)
            .thenReturn(ResponseEntity.accepted().build()); // 202 Accepted
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaOutputDTO>> obtenerCategorias(){
        // if (true) throw new InternalServerErrorException("FALLO CATEGORIA INTENCIONAL");
        return ResponseEntity.ok(this.hechosService.obtenerCategorias());
    }
}
