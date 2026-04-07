package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.FiltrosHechoInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.EstadoHecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HechoSpecification {
  public static Specification<Hecho> filtrar(FiltrosHechoInputDTO f) {
    return (root, query, cb) -> {
      // Evitar duplicados al hacer joins
      query.distinct(true);

      List<Predicate> predicates = new ArrayList<>();

      if (f.getActivo() != null) {
        predicates.add(cb.equal(root.get("activo"), f.getActivo()));
      }

      if (f.getEstado() != null) {
        predicates.add(cb.equal(root.get("estado"), f.getEstado()));
      }

      if (f.getCategorias() != null && !f.getCategorias().isEmpty()) {
        predicates.add(root.get("categoria").get("id").in(f.getCategorias()));
      }

      if (f.getFechaReporteDesde() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("fechaDeCarga"),
            f.getFechaReporteDesde().atStartOfDay()));
      }

      if (f.getFechaReporteHasta() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("fechaDeCarga"),
            endOfDay(f.getFechaReporteHasta())));
      }

      if (f.getFechaAcontecimientoDesde() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("fechaHecho"),
            f.getFechaAcontecimientoDesde().atStartOfDay()));
      }

      if (f.getFechaAcontecimientoHasta() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("fechaHecho"),
            endOfDay(f.getFechaAcontecimientoHasta())));
      }

      if (f.getLatitud() != null) {
        predicates.add(cb.equal(root.get("ubicacion").get("latitud"), f.getLatitud()));
      }

      if (f.getLongitud() != null) {
        predicates.add(cb.equal(root.get("ubicacion").get("longitud"), f.getLongitud()));
      }

      if (f.getFuentes() != null && !f.getFuentes().isEmpty()) {
        Join<Hecho, Fuente> joinFuentes = root.join("fuentes");
        predicates.add(joinFuentes.get("id").in(f.getFuentes()));
      }

      if (f.getUsuario() != null) {
        predicates.add(cb.equal(root.get("usuario"), f.getUsuario()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private static LocalDateTime endOfDay(LocalDate fecha) {
    return fecha.atTime(23, 59, 59, 999_999_999);
  }
}