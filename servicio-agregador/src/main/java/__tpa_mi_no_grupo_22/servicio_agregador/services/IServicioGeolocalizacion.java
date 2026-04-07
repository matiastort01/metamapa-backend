package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IServicioGeolocalizacion {
  Mono<Ubicacion> obtenerUbicacion(BigDecimal latitud, BigDecimal longitud);
  void poblarUbicaciones();
}
