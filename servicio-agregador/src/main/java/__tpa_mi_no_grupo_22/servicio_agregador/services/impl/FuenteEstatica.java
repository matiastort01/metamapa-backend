package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFuenteInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import __tpa_mi_no_grupo_22.servicio_agregador.mappers.HechoMapper;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IHechosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IUbicacionesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentePersistenciaService;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentes;
import __tpa_mi_no_grupo_22.servicio_agregador.services.TipoFuente;
import __tpa_mi_no_grupo_22.servicio_agregador.validators.HechoFuenteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource; // IMPORTANTE
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import reactor.core.scheduler.Schedulers;
@Component
public class FuenteEstatica implements IFuentes {
  private final WebClient webClient;
  private final HechoMapper hechoMapper;
  private final String prefijo = "FuenteEstatica";
  @Autowired
  private IFuentePersistenciaService fuentePersistenciaService;

  public FuenteEstatica(WebClient.Builder webClientBuilder, HechoMapper hechoMapper) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    this.hechoMapper = hechoMapper;
  }

  @Override
  public Mono<List<Hecho>> getHechos() {
    return webClient.get()
        .uri("/fuente-estatica/hechos")
        .retrieve()
        .bodyToFlux(HechoFuenteInputDTO.class)
        .handle(HechoFuenteValidator::validarHecho)
        .map(dto -> {
          // 1. Obtener el nombre de la fuente
          String nombreFuenteRecibida = dto.getFuenteId();
          String nombreFuenteFinal = (nombreFuenteRecibida == null || nombreFuenteRecibida.trim().isEmpty()) ? "DESCONOCIDO" : prefijo + "::" + nombreFuenteRecibida;

          // 2. Obtener la ENTIDAD Fuente (Ya gestionada/persistida)
          Fuente fuente = fuentePersistenciaService.findOrCreate(nombreFuenteFinal);

          // 3. Crear el Hecho usando el nombre de fuente garantizado
          return hechoMapper.hechoFuenteInputDTOToHecho(dto, fuente, EstadoHecho.ACEPTADO);
        })
        .collectList();
  }

  @Override
  public TipoFuente getTipo() {
    return TipoFuente.NO_PROXY;
  }
}