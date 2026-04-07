package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.DepartamentosInputDTO.DepartamentoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.DepartamentosInputDTO.DepartamentosInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.MunicipiosInputDTO.MunicipioInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.MunicipiosInputDTO.MunicipiosInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.ProvinciasInputDTO.ProvinciaInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.ProvinciasInputDTO.ProvinciasInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.UbicacionGeoRef.UbicacionGeoRefInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Departamento;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Municipio;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Provincia;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IDepartamentosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IMunicipiosRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IProvinciasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IServicioGeolocalizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class ServicioGeolocalizacion implements IServicioGeolocalizacion {
  private final WebClient webClient;
  @Autowired
  private IProvinciasRepository provinciaRepository;
  @Autowired
  private IMunicipiosRepository municipioRepository;
  @Autowired
  private IDepartamentosRepository departamentoRepository;

  // @Autowired
  public ServicioGeolocalizacion(WebClient.Builder builder) {
    this.webClient = WebClient.builder()
        .baseUrl("https://apis.datos.gob.ar/georef/api")
        .build();
  }

  @Override
  public Mono<Ubicacion> obtenerUbicacion(BigDecimal latitud, BigDecimal longitud){
    // hago la llamada a la API de Georef con las coordenadas (lat, long)
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/ubicacion")
            .queryParam("lat", latitud)
            .queryParam("lon", longitud)
            .build())
        .retrieve()
        .bodyToMono(UbicacionGeoRefInputDTO.class)
        // Reintentar si devuelve un 429
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
            .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
        .map(respuestaGeoref -> Ubicacion.builder()
            .latitud(latitud)
            .longitud(longitud)
            .provincia(new Provincia(respuestaGeoref.getUbicacion().getProvincia().getNombre()))
            .municipio(new Municipio(respuestaGeoref.getUbicacion().getMunicipio().getNombre()))
            .departamento(new Departamento(respuestaGeoref.getUbicacion().getDepartamento().getNombre()))
            .build())
        .onErrorResume(WebClientResponseException.BadRequest.class, ex -> {
          // acá ex.getResponseBodyAsString() te devuelve el JSON con los errores
          throw new IllegalArgumentException("Error de georef: " + ex.getResponseBodyAsString());
        });
  }

  @Override
  public void poblarUbicaciones() {
    // Poblar Provincias
    ProvinciasInputDTO provinciasDTO = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/provincias")
            .queryParam("max", 1000)
            .build())
        .retrieve()
        .bodyToMono(ProvinciasInputDTO.class)
        .block();

    if (provinciasDTO != null && provinciasDTO.getProvincias() != null) {
      provinciasDTO.getProvincias().forEach((ProvinciaInputDTO dto) -> {
        Provincia provincia = new Provincia();
        provincia.setNombre(dto.getNombre());
        provinciaRepository.save(provincia);
      });
    }

    // Poblar Municipios
    MunicipiosInputDTO municipiosDTO = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/municipios")
            .queryParam("max", 5000)
            .build())
        .retrieve()
        .bodyToMono(MunicipiosInputDTO.class)
        .block();

    if (municipiosDTO != null && municipiosDTO.getMunicipios() != null) {
      municipiosDTO.getMunicipios().forEach((MunicipioInputDTO dto) -> {
        Municipio municipio = new Municipio();
        municipio.setNombre(dto.getNombre());
        municipioRepository.save(municipio);
      });
    }

    // Poblar Departamentos
    DepartamentosInputDTO departamentosDTO = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/departamentos")
            .queryParam("max", 5000)
            .build())
        .retrieve()
        .bodyToMono(DepartamentosInputDTO.class)
        .block();

    if (departamentosDTO != null && departamentosDTO.getDepartamentos() != null) {
      departamentosDTO.getDepartamentos().forEach((DepartamentoInputDTO dto) -> {
        Departamento departamento = new Departamento();
        departamento.setNombre(dto.getNombre());
        departamentoRepository.save(departamento);
      });
    }
  }
}
