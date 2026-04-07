package __tpa_mi_no_grupo_22.fuente_proxy.services.impl;

import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.input.DisasterInputDTO;
import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.input.DisasterPageResponse;
import __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.output.DisasterOutputDTO;
import __tpa_mi_no_grupo_22.fuente_proxy.services.IDesastresService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DesastresService implements IDesastresService {
  private final WebClient webClient;
  private final String token;

  // Ajusta tamaño de página y retardo entre llamadas para no sobrecargar API
  private static final int DEFAULT_PAGE_SIZE = 50;
  private static final Duration PAGE_DELAY = Duration.ofMillis(200);

  public DesastresService(
      @Value("${desastres.token}") String token,
      @Value("${desastres.api.base-url}") String baseUrl) {
    this.token = token;
    this.webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build();
  }

  @Override
  public Mono<List<DisasterOutputDTO>> getAllDisasters() {
    // 1) Fetch página 1 para conocer lastPage
    return fetchPage(1)
        .flatMapMany(firstResp -> {
          int lastPage = firstResp.getLastPage();
          // datos de la primera
          Flux<DisasterInputDTO> firstData = Flux.fromIterable(firstResp.getData());
          // rango de páginas 2..lastPage
          Flux<DisasterInputDTO> restData = Flux
              .range(2, lastPage - 1)
              .flatMap(page -> fetchPage(page)
                      .flatMapMany(resp -> Flux.fromIterable(resp.getData())),
                  5
              );
          return firstData.concatWith(restData);
        })
        // mapear a DTOs de salida
        .map(this::disasterInputToOutput)
        // colectar en lista
        .collectList();
  }

  private Mono<DisasterPageResponse> fetchPage(int page) { // es mucha carga, tarda un monton en devolver los datos
    return webClient.get()
        .uri(uri -> uri
            .path("/api/desastres")
            .queryParam("page", page)
            .queryParam("per_page", DEFAULT_PAGE_SIZE)
            .build())
        .headers(h -> {
          if (!token.isEmpty()) h.setBearerAuth(token);
        })
        .retrieve()
        .bodyToMono(DisasterPageResponse.class);
  }

  private List<DisasterOutputDTO> listDisasterInputToOutput(List<DisasterInputDTO> disasterInputDTOS) {
    if (disasterInputDTOS == null || disasterInputDTOS.isEmpty()) {
      return Collections.emptyList();
    }

    return disasterInputDTOS.stream()
        .map(this::disasterInputToOutput)
        .toList();
  }

  private DisasterOutputDTO disasterInputToOutput(DisasterInputDTO disasterInputDTO) {
    return DisasterOutputDTO.builder()
        .id(disasterInputDTO.getId())
        .titulo(disasterInputDTO.getTitulo())
        .descripcion(disasterInputDTO.getDescripcion())
        .categoria(disasterInputDTO.getCategoria())
        .latitud(disasterInputDTO.getLatitud())
        .longitud(disasterInputDTO.getLongitud())
        .fecha(disasterInputDTO.getFechaHecho().toLocalDate())
        .fechaCarga(disasterInputDTO.getCreatedAt())
        .fuenteId("FP-Distaster")
        .estado("APROBADO")
        .build();
  }

  @Override
  public Mono<DisasterInputDTO> getDisasterById(Long id) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/desastres/{id}").build(id))
        .headers(h -> {
          if (!token.isEmpty()) h.setBearerAuth(token);
        })
        .retrieve()
        .bodyToMono(DisasterInputDTO.class);
  }


}