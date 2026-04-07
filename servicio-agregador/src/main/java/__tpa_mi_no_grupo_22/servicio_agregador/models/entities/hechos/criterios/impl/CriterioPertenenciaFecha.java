package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("FECHA")
@Data
public class CriterioPertenenciaFecha extends ACriterioDePertenencia {
  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDate fechaFin;

  public CriterioPertenenciaFecha(String nombreCriterio, LocalDate fechaInicio, LocalDate fechaFinal) {
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFinal;
    this.nombre = nombreCriterio;
  }

  public CriterioPertenenciaFecha() {
  }

  @Override
  public boolean perteneceA(Hecho hecho) {
    LocalDate fechaHecho = hecho.getFechaHecho().toLocalDate();

    return (fechaHecho.isAfter(fechaInicio) && fechaHecho.isBefore(fechaFin));
  }

}
