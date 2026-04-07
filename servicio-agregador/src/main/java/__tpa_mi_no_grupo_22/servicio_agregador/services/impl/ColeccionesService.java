package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ResourceNotFoundException;
import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ValidationBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.ColeccionMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.CriterioPertenenciaMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.ColeccionInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.CriterioPertenenciaInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.ColeccionOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.HechoOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.PageOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Coleccion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.ModoNavegacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.HechoMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.TipoCriterio;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaCategoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaFecha;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IColeccionesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IEstadoConsensoRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IFuentesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IColeccionesService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentePersistenciaService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentesService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    @Autowired
    private IColeccionesRepository coleccionesRepository;
    @Autowired
    private IEstadoConsensoRepository estadoConsensoRepository;
    @Autowired
    private IFuentesRepository fuentesRepository;
    @Autowired
    private ICategoriasRepository categoriasRepository;
    @Autowired
    private IHechosService hechosService;
    @Autowired
    private HechoMapper hechoMapper;
    @Autowired
    private ColeccionMapper coleccionMapper;
    @Autowired
    private CriterioPertenenciaMapper criterioPertenenciaMapper;

    private Coleccion obtenerColeccionDominio(Long id){
        return coleccionesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la colección con id: " + id));
    }

    @Override
    public ColeccionOutputDTO obtenerColeccion(Long id) {
        Coleccion coleccion = this.obtenerColeccionDominio(id);

        return this.coleccionMapper.toColeccionOutputDTO(coleccion);
    }

    @Override
    public List<ColeccionOutputDTO> obtenerColecciones(List<Long> ids) {
        if( ids == null || ids.isEmpty() ){
            return coleccionesRepository
                .findAll()
                .stream()
                .map(coleccionMapper::toColeccionOutputDTO)
                .toList();
        }
        else{
            return coleccionesRepository
                .findAll()
                .stream()
                .filter(coleccion -> ids.contains(coleccion.getId()))
                .map(coleccionMapper::toColeccionOutputDTO)
                .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HechoOutputDTO> obtenerHechosAsociadosColeccion(Long id, FiltrosHechoInputDTO filtros, ModoNavegacion modoNavegacion) {
        return this.obtenerHechosFiltradosParaColeccion(id, filtros, modoNavegacion).stream()
            .map(hechoMapper::hechoToHechoOutputDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageOutputDTO<HechoOutputDTO> obtenerHechosAsociadosColeccionPaginado(Long id, FiltrosHechoInputDTO filtros, ModoNavegacion modo, Integer page, Integer size) {
        // Obtengo la lista filtrada de los hechos asociados a la coleccion y que cumplen con los filtros (Reutilizando lógica)
        List<Hecho> listaTotal = obtenerHechosFiltradosParaColeccion(id, filtros, modo);

        // Como parte del filtrado se realiza en memoria, no pude reutilizar la lógica de paginado del HechosService (pagina Spring al pedir los hechos), por lo que aca debo paginar en memoria
        int start = page * size;
        int end = Math.min((start + size), listaTotal.size());

        List<Hecho> listaPaginada;

        if (start > listaTotal.size()) {
            // Si piden la página 10 y solo hay 2 páginas, devolvemos vacío
            listaPaginada = List.of();
        } else {
            // Cortamos la sub-lista correspondiente a la página
            listaPaginada = listaTotal.subList(start, end);
        }

        // Mapeo a OutputDTO los elementos de la página
        List<HechoOutputDTO> hechosOutputDTO = listaPaginada.stream()
            .map(hechoMapper::hechoToHechoOutputDTO)
            .toList();

        // Construyo el objeto Page de Spring
        // PageImpl(contenido, pageable, total_elementos_reales)
        Page<HechoOutputDTO> pageResult = new PageImpl<>(
            hechosOutputDTO,
            PageRequest.of(page, size),
            listaTotal.size() // El total real filtrado
        );

        // Devuelvo mi formato de pagina
        return new PageOutputDTO<>(pageResult);
    }

    // TODO: ESTO PUEDE LLEGAR A DAR PROBLEMAS POR OBTENER HECHOS DOMINIO. POSIBLE SOLUCION: PONER PUBLICO EL METODO Y AGREGARLE UN @OVERRIDE Y UN @TRANSACTON(READONLY = TRUE) PARA MANTENER LA TRANSACCION ABIERTA
    private List<Hecho> obtenerHechosFiltradosParaColeccion(Long id, FiltrosHechoInputDTO filtros, ModoNavegacion modoNavegacion){
        // Obtengo la coleccion
        Coleccion coleccion = this.obtenerColeccionDominio(id);

        // Fusiono filtros de fuentes para traer hechos unicamente de las fuentes de la coleccion, pero tambien atenerse al filtro del usuario (tal vez no quiere ver los hechos de TODAS las fuentes de la coleccion)
        aplicarFiltroDeFuentes(filtros, coleccion.getFuentes());

        // Obtengo los hechos asociados a la coleccion que cumplen con los filtros
        List<Hecho> hechos = hechosService.obtenerHechosDominio(filtros);

        // Filtro los hechos consensuados
        if( modoNavegacion == ModoNavegacion.CURADA && coleccion.getTipoAlgoritmoConsenso() != null ){
            // Hago 1 SOLA consulta a la BD para traer la "Lista Blanca" de títulos de esta coleccion y tipo de algoritmo, en lugar de preguntar N veces si el hecho esta consensuado (N consultas a la BD)
            Set<String> titulosConsensuados = estadoConsensoRepository.findTitulosConsensuados(
                coleccion.getTitulo(),
                coleccion.getTipoAlgoritmoConsenso()
            );

            // Filtro los hechos
            hechos = hechos.stream()
                .filter(h -> titulosConsensuados.contains(h.getTitulo()))
                .toList();
        }

        //  Filtro los hechos que cumplen con los criterios
        if (coleccion.getCriterios() != null && !coleccion.getCriterios().isEmpty()) { // Nunca debería suceder que sea null o este vacia por regla de negocio pero porlas
            hechos = hechos.stream()
                .filter(h -> cumpleCriterios(h, coleccion.getCriterios()))
                .toList();
        }

        return hechos;
    }

    private void aplicarFiltroDeFuentes(FiltrosHechoInputDTO filtros, Set<Fuente> fuentesColeccion) {
        List<Long> idsFuentesColeccion = fuentesColeccion.stream().map(Fuente::getId).toList();

        if (filtros.getFuentes() == null || filtros.getFuentes().isEmpty()) {
            // Caso A: Usuario no filtra -> Usamos todas las fuentes de la colección
            filtros.setFuentes(new ArrayList<>(idsFuentesColeccion));
        }
        else {
            // Caso B: Usuario filtra -> se debe hacer una interseccion entre las fuentes de la coleccion y lo q pide el usuario, para eso uso retainAll
            // Si el usuario pide menos, retainAll deja solo esas. Ya que se queda con aquellas que esten en ambos conjuntos.
            filtros.getFuentes().retainAll(idsFuentesColeccion);

            // Caso C: Si la intersección es vacía (el usuario pidió por algún motivo una fuente ajena a la coleccion, por ende no hay nada en la intersección), forzamos vacío en SQL
            if(filtros.getFuentes().isEmpty()) {
                // Si dejo la lista vacía va a traer los hechos de todas las fuentes, por eso le agrego un ID imposible (-1L -> la L es pora indicar q el tipo de dato es Long) para que no traiga ningun hecho, ya que no existe ninguna fuente con ese id
                filtros.getFuentes().add(-1L);
            }
        }
    }

    private boolean cumpleCriterios(Hecho hecho, List<ACriterioDePertenencia> criterios) {
        if (criterios == null || criterios.isEmpty()) return true;
        return criterios.stream().allMatch(c -> c.perteneceA(hecho));
    }

    @Override
    @Transactional
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO) {
        Map<String, String> errores = new HashMap<>();

        // Título Único
        if (coleccionesRepository.findByTituloIgnoreCase(coleccionInputDTO.getTitulo()).isPresent()) {
            errores.put("titulo", "Ya existe una colección con el título: " + coleccionInputDTO.getTitulo());
        }

        if (!errores.isEmpty()) {
            // Lanza la excepción que se mapeará a HTTP 422 y que contiene todos los errores de campo acumulados
            throw new ValidationBusinessException(errores);
        }

        // Crear y guardar la nueva colección
        Coleccion nueva = coleccionMapper.toColeccion(coleccionInputDTO);

        nueva.setFechaCreacion(LocalDateTime.now());

        this.actualizarFuentesColeccion(nueva, coleccionInputDTO.getFuentesIds());

        this.actualizarCriterios(nueva, coleccionInputDTO.getCriteriosDePertenencias());

        Coleccion guardada = coleccionesRepository.save(nueva);

        return coleccionMapper.toColeccionOutputDTO(guardada);
    }

    @Override
    public void eliminarColeccion(Long id) {
        this.coleccionesRepository.deleteById(id); // TODO: DEBERIA SER BAJA LOGICA?
    }

    @Override
    @Transactional
    public ColeccionOutputDTO editarColeccion(Long id, ColeccionInputDTO coleccionInputDTO) {
        Map<String, String> errores = new HashMap<>();

        // Buscamos la colección existente
        Coleccion coleccion = this.obtenerColeccionDominio(id);

        // Validacion de negocio
        if (!Objects.equals(coleccionInputDTO.getTitulo(), coleccion.getTitulo())) {
            // Validamos que el NUEVO título no exista ya en otra colección
            if (this.coleccionesRepository.findByTituloIgnoreCase(coleccionInputDTO.getTitulo()).isPresent()) {
                errores.put("titulo", "El título '" + coleccionInputDTO.getTitulo() + "' ya existe en otra colección.");
            }
        }

        if (!errores.isEmpty()) {
            throw new ValidationBusinessException(errores);
        }

        // Editar TÍTULO (Solo si es diferente al actual)
        coleccion.setTitulo(coleccionInputDTO.getTitulo());

        // Editar DESCRIPCIÓN
        coleccion.setDescripcion(coleccionInputDTO.getDescripcion());

        // Editar ALGORITMO (Solo si no es nulo ni vacío)
        if (coleccionInputDTO.getAlgoritmoConsenso() != null) {
            coleccion.setTipoAlgoritmoConsenso(coleccionInputDTO.getAlgoritmoConsenso());
        }

        // Editar FUENTES (Solo si la lista no es nula)
        this.actualizarFuentesColeccion(coleccion, coleccionInputDTO.getFuentesIds());

        // Editar CRITERIOS DE PERTENENCIA
        this.actualizarCriterios(coleccion, coleccionInputDTO.getCriteriosDePertenencias());

        // Guardar cambios
        Coleccion guardada = this.coleccionesRepository.save(coleccion);

        return coleccionMapper.toColeccionOutputDTO(guardada);
    }

    private void actualizarFuentesColeccion(Coleccion coleccion, List<Long> idsSolicitados) {
        // Agrego validacion por si en algun momento se reutiliza el método, pero en crear y editar ya se verifica antes de llamarlo
        if (idsSolicitados == null || idsSolicitados.isEmpty()) {
            throw new ValidationBusinessException(
                Map.of("fuentes", "Debe seleccionar al menos una fuente para la colección.")
            );
        }

        // Eliminar duplicados del input para evitar errores de conteo
        Set<Long> idsUnicosSolicitados = new HashSet<>(idsSolicitados);

        // Valido si todas las fuentes del input realmente existen en la BD (si el tamaño de las existentes es menor es porq hay fuentes en el input que no existen)
        List<Fuente> fuentesExistentes = fuentesRepository.findAllById(idsUnicosSolicitados);

        if (fuentesExistentes.size() < idsUnicosSolicitados.size()) {
            // Calculamos la diferencia para ser específicos en el error
            Set<Long> idsEncontrados = fuentesExistentes.stream()
                .map(Fuente::getId)
                .collect(Collectors.toSet());

            List<Long> idsFaltantes = idsUnicosSolicitados.stream()
                .filter(id -> !idsEncontrados.contains(id))
                .toList();

            throw new ValidationBusinessException(
                Map.of("fuentes", "Las siguientes fuentes no existen: " + idsFaltantes)
            );
        }

        // Actualizar la colección. Reemplazamos el Set completo por si se eliminaron fuentes (si solo agregase no estaría dando la posibilidad de que se quiten fuentes)
        coleccion.setFuentes(new HashSet<>(fuentesExistentes));
    }

    private void actualizarCriterios(Coleccion coleccion, List<CriterioPertenenciaInputDTO> criteriosPertenenciaInputDTO) {
        // Agrego validacion por si en algun momento se reutiliza el método, pero en crear y editar ya se verifica antes de llamarlo
        if (criteriosPertenenciaInputDTO == null || criteriosPertenenciaInputDTO.isEmpty()) {
            throw new ValidationBusinessException(
                Map.of("criteriosDePertenencia", "Debe definir al menos un criterio de pertenencia.")
            );
        }

        // Validación de nombres duplicados
        Set<String> nombres = new HashSet<>();
        for (CriterioPertenenciaInputDTO criterioPertenenciaInputDTO : criteriosPertenenciaInputDTO) {
            if (nombres.contains(criterioPertenenciaInputDTO.getNombreCriterio())) {
                throw new ValidationBusinessException(
                    Map.of("criteriosDePertenencia", "No puede haber dos criterios con el mismo nombre: " + criterioPertenenciaInputDTO.getNombreCriterio())
                );
            }

            nombres.add(criterioPertenenciaInputDTO.getNombreCriterio());
        }

        // Obtengo los Ids del input que no son nulls (si es null es porq es un criterio nuevo y debo crearlo)
        Set<Long> idsInput = criteriosPertenenciaInputDTO.stream()
            .map(CriterioPertenenciaInputDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // Remuevo de la lista de criterios de la coleccion aquellos criterios no que no vinieron en el input (fueron eliminados). Al removerlos de la lista orphanRemoval los borrará de la BD.
        coleccion.getCriterios().removeIf(c -> c.getId() != null && !idsInput.contains(c.getId())); // si esta seteado el criterio nunca deberia ser null pero bueno, porlas

        // Crear o actualizar los criterios
        for (CriterioPertenenciaInputDTO criterioPertenenciaInputDTO : criteriosPertenenciaInputDTO) {
            // Validamos que los datos del criterio existan en la BD (ej: Categoría)
            this.validarReglasCriterio(criterioPertenenciaInputDTO);

            if (criterioPertenenciaInputDTO.getId() == null) {
                // CRITERIO NUEVO -> CREAR
                // Mapeo
                ACriterioDePertenencia nuevoCriterio = this.criterioPertenenciaMapper.toCriterioPertenencia(criterioPertenenciaInputDTO);

                // Agregar criterio
                coleccion.getCriterios().add(nuevoCriterio);
            }
            else {
                // CRITERIO EXISTENTE -> ACTUALIZAR
                // Obtengo el criterio ya existente
                ACriterioDePertenencia criterioExistente = coleccion.getCriterios().stream()
                    .filter(c -> c.getId().equals(criterioPertenenciaInputDTO.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ValidationBusinessException(
                        Map.of("criteriosDePertenencia", "El criterio de ID " + criterioPertenenciaInputDTO.getId() + " no pertenece a esta colección.")
                    ));

                // Actualizo los valores del criterio existente
                if (esMismoTipo(criterioExistente, criterioPertenenciaInputDTO.getTipoCriterio())) {
                    // Si el criterio NO cambio de tipo => actualizo los campos pertinentes al tipo, manteniendo su ID
                    actualizarValoresCriterio(criterioExistente, criterioPertenenciaInputDTO);
                }
                else {
                    // Si el criterio SI cambio de tipo => debo reemplazar por completo el criterio para evitar problemas con JPA (como JPA asocio la entrada a un tipo al cargar el hecho, no le puedo cambiar el campo -> generaria problemas de matcheo de tipos (inconsistencia))
                    // Remuevo el criterio de la lista
                    coleccion.getCriterios().remove(criterioExistente);

                    // Creo un nuevo criterio con el tipo nuevo
                    criterioPertenenciaInputDTO.setId(null); // Le seteo el ID en null para que se genere otro nuevo al añadirlo (creo q no es necesario)
                    ACriterioDePertenencia nuevoCriterio = this.criterioPertenenciaMapper.toCriterioPertenencia(criterioPertenenciaInputDTO);

                    // Agrego el criterio
                    coleccion.getCriterios().add(nuevoCriterio);
                }
            }
        }
    }

    private void validarReglasCriterio(CriterioPertenenciaInputDTO dto) {
        if (dto.getTipoCriterio() == TipoCriterio.CATEGORIA) {
            // Obtengo el nombre de la categoría del mapa de parámetros
            Object categoriaRaw = dto.getParametros().get("categoria");

            // La validación de nulidad ya la hace el mapper, pero por las dudas verifico
            if (categoriaRaw != null) {
                String nombreCategoria = categoriaRaw.toString().trim();

                // Verifico que la categoria exista
                if (categoriasRepository.findByNombreIgnoreCase(nombreCategoria).isEmpty()) { // TODO: Si me llega el id mejor q mejor
                    throw new ValidationBusinessException(
                        Map.of("criterioCategoria", "La categoría '" + nombreCategoria + "' no existe en el sistema.")
                    );
                }
            }
        }
        // Aquí puedes agregar más `case` si otros criterios requieren validación contra BD
    }

    private boolean esMismoTipo(ACriterioDePertenencia criterioDePertenencia, TipoCriterio tipoCriterio) {
        return switch (tipoCriterio) {
            case FECHA -> criterioDePertenencia instanceof CriterioPertenenciaFecha;
            case CATEGORIA -> criterioDePertenencia instanceof CriterioPertenenciaCategoria;
            default -> false;
        };
    }

    private void actualizarValoresCriterio(ACriterioDePertenencia criterioExistente, CriterioPertenenciaInputDTO criterioPertenenciaInputDTO) {
        // 1. VALIDACIÓN DE TIPO (Integridad)
        // No permitimos que un criterio cambie de tipo manteniendo el mismo ID por cuestiones de integridad.
        // Si bien la validación ya esta en actualizar criterio la agrego también acá por las dudas.
        if (!esMismoTipo(criterioExistente, criterioPertenenciaInputDTO.getTipoCriterio())) {
            throw new ValidationBusinessException(
                Map.of("criteriosDePertenencias", "Error de integridad: No se puede cambiar el tipo de criterio manteniendo el mismo ID.")
            );
        }

        // Creamos un objeto "temporal" solo para validar y parsear los datos nuevos (la lógica está en el mapper, por eso se utiliza esta forma).
        ACriterioDePertenencia criterioNuevoTemporal = criterioPertenenciaMapper.toCriterioPertenencia(criterioPertenenciaInputDTO);

        // Actualizamos los valores del criterio existente en función del input
        // NOMBRE
        criterioExistente.setNombre(criterioNuevoTemporal.getNombre());

        // PARAMETROS
        switch (criterioPertenenciaInputDTO.getTipoCriterio()) {
            case FECHA -> {
                CriterioPertenenciaFecha nuevoFecha = (CriterioPertenenciaFecha) criterioNuevoTemporal;
                CriterioPertenenciaFecha existenteFecha = (CriterioPertenenciaFecha) criterioExistente;

                existenteFecha.setFechaInicio(nuevoFecha.getFechaInicio());
                existenteFecha.setFechaFin(nuevoFecha.getFechaFin());
            }
            case CATEGORIA -> {
                CriterioPertenenciaCategoria nuevoCategoria = (CriterioPertenenciaCategoria) criterioNuevoTemporal;
                CriterioPertenenciaCategoria existenteCategoria = (CriterioPertenenciaCategoria) criterioExistente;

                existenteCategoria.setCategoria(nuevoCategoria.getCategoria());
            }
            // Uso el default para lanzar un error de aviso por si se agregan más tipos en el futuro y no se implementaron acá
            default -> throw new IllegalStateException("Tipo de criterio no manejado: " + criterioExistente.getClass());
        }
    }
}
