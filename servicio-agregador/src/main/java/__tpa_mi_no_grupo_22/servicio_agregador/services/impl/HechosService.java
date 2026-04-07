package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ResourceNotFoundException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ValidationBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.CategoriaMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFrontInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.CategoriaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.PageOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.HechoMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IHechosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.impl.HechoSpecification;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentes;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IHechosService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.INormalizador;
import __tpa_mi_no_grupo_22.servicio_agregador.services.TipoFuente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class HechosService implements IHechosService {
  private static final Logger log = LoggerFactory.getLogger(HechosService.class);

  @Autowired
  private IHechosRepository hechosRepository;

  @Autowired
  private ICategoriasRepository categoriasRepository;

  @Autowired
  private INormalizador normalizador;

  @Autowired
  private HechoMapper hechoMapper;

  @Autowired
  private CategoriaMapper categoriaMapper;

  @Autowired
  private List<IFuentes> fuentes;

  @Autowired
  @Lazy // Importante @Lazy para evitar error de dependencia circular
  private HechosService self; // Para poder llamarse a si mismo y activar el @Transactional (si hago this. no se abre la transaccion)

  // SEMÁFORO PARA CONTROLAR CONCURRENCIA (evita mas de una actualizacion de hechos simultanea)
  private final Map<TipoFuente, AtomicBoolean> semaforos = new ConcurrentHashMap<>();

  // Método para obtener hechos con lógica de negocio (ACTUALIZA PROXY)
  @Override
  @Transactional(readOnly = true)
  public List<HechoOutputDTO> obtenerHechos(FiltrosHechoInputDTO filtros) {
    return this.obtenerHechosDominio(filtros).stream()
        .map(hechoMapper::hechoToHechoOutputDTO)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Hecho> obtenerHechosDominio(FiltrosHechoInputDTO filtros) {
    // Lanzar actualización en paralelo SIN bloquear la request
    this.actualizarHechosFuente(TipoFuente.PROXY)
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError(e -> log.error("Error actualizando hechos.", e))
        .subscribe();

    // Consulta optimizada en SQL
    Specification<Hecho> spec = HechoSpecification.filtrar(filtros);

    // Filtro los hechos
    List<Hecho> hechosLazy = hechosRepository.findAll(spec);

    // Hidrato únicamente los hechos de la filtrados (le cargo fuentes y multimedia)
    return this.hidratarHechos(hechosLazy);
  }

  private List<Hecho> hidratarHechos(List<Hecho> hechosLazy) {
    if (hechosLazy.isEmpty()) return List.of();

    // Extraigo los IDs
    List<Long> ids = hechosLazy.stream().map(Hecho::getId).toList();

    // Retorno los hechos hidratados con la Query optimizada con JOIN FETCH
    return this.hechosRepository.findByIdIn(ids); // find by id in desordena -> si lo quiero ordenado habria q hacerlo antes de devolver los hechos
  }

  @Override
  @Transactional(readOnly = true)
  public PageOutputDTO<HechoOutputDTO> obtenerHechosPaginado(FiltrosHechoInputDTO filtros, Integer page, Integer size) {
    // Lanzar actualización en paralelo SIN bloquear la request
    this.actualizarHechosFuente(TipoFuente.PROXY)
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError(e -> log.error("Error actualizando hechos.", e))
        .subscribe();

    // Se construye la pagina
    Pageable pageable = PageRequest.of(page, size, Sort.by("fechaDeCarga").descending());

    // Se construye la Specification con los filtros del usuario
    Specification<Hecho> spec = HechoSpecification.filtrar(filtros);

    // Consulta a la Base de Datos. JPA se encarga de filtrar y paginar
    Page<Hecho> pageHechosLazy = hechosRepository.findAll(spec, pageable);

    // Hidrato únicamente los hechos de la página (le cargo fuentes y multimedia)
    List<Hecho> hechos = this.hidratarHechos(pageHechosLazy.getContent());

    // Mapeo los hechos a OutputDTO
    List<HechoOutputDTO> hechosOutputDTO = hechos.stream()
        .map(hechoMapper::hechoToHechoOutputDTO)
        .toList();

    // Retorno el formato personalizado de página
    return new PageOutputDTO<>(new PageImpl<>(hechosOutputDTO, pageable, pageHechosLazy.getTotalElements()));
  }

  @Override
  public HechoOutputDTO obtenerHecho(Long id) {
    return this.hechoMapper.hechoToHechoOutputDTO(this.obtenerHechoDominio(id));
  }

  @Override
  public Hecho obtenerHechoDominio(Long id){
    return this.hechosRepository.findByIdConFuentesYMultimedia(id)
        .orElseThrow(() -> new ResourceNotFoundException("No se encontró el hecho con id: " + id));
  }

  @Override
  public HechoOutputDTO obtenerUltimo() {
    return this.hechosRepository.findTopByOrderByIdDesc()
        .map(hecho -> hechoMapper.hechoToHechoOutputDTO(hecho))
        .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningún hecho."));
  }

  @Override
  public Mono<Void> actualizarHechosFuente(TipoFuente tipoFuente) {
    AtomicBoolean semaforo = semaforos.computeIfAbsent(tipoFuente, k -> new AtomicBoolean(false));

    if (!semaforo.compareAndSet(false, true)) {
      log.info("Ya existe una actualización en curso para {}. Se omite esta ejecución.", tipoFuente);
      return Mono.empty(); // Salimos inmediatamente
    }

    log.info("Iniciando actualización de hechos para la fuente tipo: {}", tipoFuente.name());

    // Filtro las fuentes que corresponden
    List<IFuentes> fuentesSeleccionadas = fuentes.stream()
        .filter(f -> f.getTipo().equals(tipoFuente))
        .toList();

    if (fuentesSeleccionadas.isEmpty()) {
      log.warn("No se encontraron fuentes activas del tipo {}. Finalizando proceso.", tipoFuente);
      semaforo.set(false);
      return Mono.empty();
    }

    return Flux.fromIterable(fuentesSeleccionadas)
        .doOnSubscribe(s -> log.info("Suscrito a la actualización de hechos para: {}", tipoFuente))
        .flatMap(fuente ->
            fuente.getHechos()
                // Si esta fuente específica falla (ej. Connection Refused), atrapamos el error, logueamos y devolon la siguivemos vacío para seguir cente fuente.
                .onErrorResume(e -> {
                  log.error("Error al conectarse con fuente del tipo {}. Error: {}", fuente.getTipo(), e.getMessage());
                  return Mono.empty(); // Ignoramos esta fuente y seguimos
                })
        )
        .doOnNext(list -> log.info("Recibí {} hechos de una fuente tipo {}", list.size(), tipoFuente))
        .flatMapIterable(list -> list)
        .flatMap(hecho ->
            this.guardarHechoNormalizado(hecho)
                .onErrorResume(e -> {
                  log.error("Error guardando el hecho '{}'. Error: {}", hecho.getTitulo(), e.getMessage());
                  return Mono.empty();
                })
        )
        .doOnError(e -> log.error("Error durante la actualización de hechos para fuente tipo {}", tipoFuente, e))
        .doFinally(signalType -> {
          log.info("Proceso de actualización finalizado (Signal: {}). Liberando semáforo.", signalType);
          semaforo.set(false);
        })
        .then();
  }

  // Método Guardar Normalizado (Busca por Título)
  @Override
  public Mono<Hecho> guardarHechoNormalizado(Hecho hecho) {
    return normalizador.normalizar(hecho)
        .flatMap(hechoNormalizado ->
            Mono.fromCallable(() ->
                    // IMPORTANTE: Usamos 'self' en lugar de 'this' para que active la Transacción
                    self.actualizarOInsertarHecho(hechoNormalizado)
                )
                .subscribeOn(Schedulers.boundedElastic())
        );
  }

  @Transactional
  public Hecho actualizarOInsertarHecho(Hecho hechoNormalizado) {
    Optional<Hecho> optionalExisting = this.hechosRepository.findByTitulo(hechoNormalizado.getTitulo());

    if (optionalExisting.isPresent()) {
      // --- CASO ACTUALIZACIÓN ---
      Hecho hechoExistente = optionalExisting.get();

      hechoExistente.setDescripcion(hechoNormalizado.getDescripcion());
      hechoExistente.setCategoria(hechoNormalizado.getCategoria());
      hechoExistente.setUbicacion(hechoNormalizado.getUbicacion());
      hechoExistente.setFechaHecho(hechoNormalizado.getFechaHecho());
      hechoExistente.getFuentes().addAll(hechoNormalizado.getFuentes()); // Si el hecho con cambios proviene de una fuente distinta, se sumara a las fuentes del hecho
      hechoExistente.setUsuario(hechoNormalizado.getUsuario());

      // Recorremos las fotos NUEVAS que trae esta fuente
      if (hechoNormalizado.getMultimedia() != null) {
        for (String fotoNueva : hechoNormalizado.getMultimedia()) {
          // Solo agregamos si NO existe ya (evita duplicados exactos)
          if (!hechoExistente.getMultimedia().contains(fotoNueva)) {
            hechoExistente.getMultimedia().add(fotoNueva);
          }
        }
      }

      return hechosRepository.save(hechoExistente);

    } else {
      // --- CASO INSERT ---
      hechoNormalizado.setActivo(true);
      hechoNormalizado.setFechaDeCarga(LocalDateTime.now());

      return hechosRepository.save(hechoNormalizado);
    }
  }

  // Método Editar Hecho (Por ID - Más rápido)
  @Transactional
  public HechoOutputDTO editarHecho(Long id, HechoFrontInputDTO hechoEditado) {
    // Busco el hecho
    Hecho hechoExistente = hechosRepository.findByIdConFuentesYMultimedia(id)
        .orElseThrow(() -> new ResourceNotFoundException("No se encontró el hecho con id: " + id));

    // Validaciones
    Map<String, String> errores = new HashMap<>();

    // Titulo duplicado
    boolean cambioDeTitulo = !hechoEditado.getTitulo().trim().equalsIgnoreCase(hechoExistente.getTitulo());
    if (cambioDeTitulo) {
      // Verificamos si el nuevo título ya existe en CUALQUIER OTRO hecho (excluyendo el actual)
      if (hechosRepository.existsByTituloAndIdNot(hechoEditado.getTitulo(), hechoExistente.getId())) {
        errores.put("titulo", "El título '" + hechoEditado.getTitulo() + "' ya pertenece a otro hecho.");
      }
    }

    if (!errores.isEmpty()) {
      throw new ValidationBusinessException(errores);
    }

    // Normalizo el hecho
    Hecho hechoNormalizado = normalizador.normalizar(hechoMapper.hechoFrontInputDTOToHecho(hechoEditado)).block();

    // TODO: SI EL HECHO CAMBIA DE TITULO, SE DEBERIA CREAR UN NUEVO HECHO APARTE Y RETIRAR LA FUENTE DEL HECHO ASOCIADO AL TITULO ANTERIOR (SERIA DINAMICA ESA FUENTE -> QUEDA RARO PORQ LO TENGO Q HARDCODEAR -> COMO NO SE CAMBIA EN DINAMICA AL PEDIR HECHOS SE VA A DUPLICAR EL HECHO PORQ VA A VENIR DE DINAMICA CON EL TITULO VIEJO -> LAS EDICIONES TENDRIAN Q SER EN DINAMICA? SI SON EN DINAMICA DE TODAS FORMAS SE VAN A DUPLICAR)
    // SI NO HAGO ESTO VOY A TENER UN HECHO CON TITULO DISTINTO, PERO Q TIENE ASOCIADAS FUENTES Q NO TRAJERON EL HECHO CON ESE TITULO

//    if (cambioDeTitulo) {
//      // CASO A: EL TÍTULO CAMBIÓ -> CREAR NUEVO, MIGRAR FUENTE
//
//      // 1. Instanciamos el nuevo hecho limpio
//      Hecho nuevoHecho = new Hecho();
//
//      // 2. Seteamos los datos que vienen del input (del "hechoInput/Editado")
//      nuevoHecho.setTitulo(hechoInput.getTitulo());
//      nuevoHecho.setDescripcion(hechoInput.getDescripcion());
//      nuevoHecho.setCategoria(hechoInput.getCategoria());
//      nuevoHecho.setUbicacion(hechoInput.getUbicacion());
//      nuevoHecho.setMultimedia(hechoInput.getMultimedia());
//      nuevoHecho.setFechaHecho(hechoInput.getFechaHecho());
//      nuevoHecho.setUsuario(hechoInput.getUsuario());
//
//      // 3. LOGICA DE FUENTES (MIGRACIÓN)
//      // Las fuentes que vienen en el request son las que están "provocando" este cambio.
//      // Deben irse del hecho viejo y pasar al nuevo.
//      Set<Fuente> fuentesDelInput = hechoInput.getFuentes();
//
//      // A. Las asignamos al nuevo hecho
//      nuevoHecho.setFuentes(new HashSet<>(fuentesDelInput));
//
//      // B. Las "sacamos" del hecho anterior (Esto cumple: "retirar la fuente del hecho asociado al titulo anterior")
//      // IMPORTANTE: Tus objetos 'Fuente' deben tener equals() y hashCode() implementados por ID.
//      hechoExistente.getFuentes().removeAll(fuentesDelInput);
//
//      // 4. Guardamos ambos estados
//      hechosRepository.save(hechoExistente); // Guardamos el viejo con las fuentes removidas
//      hechoParaRetornar = hechosRepository.save(nuevoHecho); // Guardamos el nuevo
//
//    } else {
//      // CASO B: EL TÍTULO SE MANTIENE -> ACTUALIZACIÓN NORMAL
//
//      hechoExistente.setDescripcion(hechoInput.getDescripcion());
//      hechoExistente.setCategoria(hechoInput.getCategoria());
//      hechoExistente.setUbicacion(hechoInput.getUbicacion());
//      hechoExistente.setMultimedia(hechoInput.getMultimedia());
//      hechoExistente.setFechaHecho(hechoInput.getFechaHecho());
//      hechoExistente.setUsuario(hechoInput.getUsuario());
//
//      // Aquí solo agregamos las fuentes nuevas si las hubiera, sin borrar las históricas
//      hechoExistente.getFuentes().addAll(hechoInput.getFuentes());
//
//      hechoParaRetornar = hechosRepository.save(hechoExistente);
//    }E

    // Actualizo los campos
    hechoExistente.setTitulo(hechoNormalizado.getTitulo());
    hechoExistente.setDescripcion(hechoNormalizado.getDescripcion());
    hechoExistente.setCategoria(hechoNormalizado.getCategoria());
    hechoExistente.setUbicacion(hechoNormalizado.getUbicacion());
    hechoExistente.setMultimedia(hechoNormalizado.getMultimedia());
    hechoExistente.setFechaHecho(hechoNormalizado.getFechaHecho());
    hechoExistente.setUsuario(hechoNormalizado.getUsuario());
    hechoExistente.getFuentes().addAll(hechoNormalizado.getFuentes()); // TODO: NO SE CONTEMPLA LA POSIBILIDAD DE QUE UNA FUENTE BORRE UN HECHO Y YA NO PERTENEZCA A ESA FUENTE

    // Guardamos el hecho y lo retornamos
    Hecho hechoGuardado = hechosRepository.save(hechoExistente);

    return hechoMapper.hechoToHechoOutputDTO(hechoGuardado);
  }

  @Override
  public Void aprobarHecho(Long id) {
    Hecho hecho = this.obtenerHechoDominio(id);

    hecho.setEstado(EstadoHecho.ACEPTADO);

    hechosRepository.save(hecho);

    return null;
  }

  @Override
  public Void rechazarHecho(Long id) {
    Hecho hecho = this.obtenerHechoDominio(id);

    hecho.setEstado(EstadoHecho.RECHAZADO);

    hechosRepository.save(hecho);

    return null;
  }

  @Override
  public List<CategoriaOutputDTO> obtenerCategorias(){
    return this.categoriasRepository.findAll().stream()
        .map(this.categoriaMapper::toCategoriaOutputDTO)
        .toList();
  }
}