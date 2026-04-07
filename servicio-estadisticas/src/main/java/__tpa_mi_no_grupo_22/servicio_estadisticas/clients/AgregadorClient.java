package __tpa_mi_no_grupo_22.servicio_estadisticas.clients;

import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.ColeccionInputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.HechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input.PageInputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class AgregadorClient {

    private static final Logger log = LoggerFactory.getLogger(AgregadorClient.class);
    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8081/metamapa";
    public AgregadorClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }
    // ============= Helpers genéricos =============

    private <T> List<T> getList(String path, Class<T> clazz) {
        try {
            List<T> result = webClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToFlux(clazz)
                    .collectList()
                    .block();
            log.info("✔ GET {} -> {} elementos recibidos", path, result != null ? result.size() : 0);
            return result != null ? result : Collections.emptyList();

        } catch (WebClientResponseException e) {
            log.error("❌ Error HTTP {} al obtener {}: {}", e.getStatusCode(), path, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("⚠️ Error inesperado al obtener {}: {}", path, e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    public List<HechoInputDTO> obtenerHechos() {
        return getList("/hechos", HechoInputDTO.class);
    }

    public List<ColeccionInputDTO> obtenerColecciones() {
        return getList("/colecciones", ColeccionInputDTO.class);
    }


//en el front el metodo que llama a los hechos de una coleccion esta asi
    public List<HechoInputDTO> obtenerHechosColeccion(Long id) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/colecciones/estadistica/{id}/hechos");

        String uriFinal = builder.buildAndExpand(id).toUriString();

        try {
            List<HechoInputDTO>hechos = webClient
                    .get()
                    .uri(uriFinal)
                    .retrieve()
                    .bodyToFlux(HechoInputDTO.class)
                    .collectList()
                    .block();
            //hechos.forEach(h-> log.info("hecho obtenido: "+ h.getTitulo()));
            return   hechos;
            }
            catch (Exception e) {
                log.error("🔥 Error de conexión con módulo externo: {}", e.getMessage());
            return Collections.emptyList();
        }
        }

}
