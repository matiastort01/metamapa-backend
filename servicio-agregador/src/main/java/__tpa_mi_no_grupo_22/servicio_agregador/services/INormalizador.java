package __tpa_mi_no_grupo_22.servicio_agregador.services;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Departamento;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Municipio;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Provincia;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Ubicacion;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface INormalizador {
  Mono<Hecho> normalizar(Hecho hecho);
  Categoria normalizarCategoria(Categoria categoriaInput);
  String normalizarTitulo(String titulo);
  LocalDateTime normalizarFecha(LocalDateTime fecha);
  LocalDateTime normalizarFecha(LocalDate fecha);
  LocalDateTime normalizarFecha(Instant fecha);
  Mono<Ubicacion> normalizarUbicacion(Ubicacion ubicacion);
  Provincia normalizarProvincia(Provincia provincia);
  Municipio normalizarMunicipio(Municipio municipio);
  Departamento normalizarDepartamento(Departamento departamento);
}
