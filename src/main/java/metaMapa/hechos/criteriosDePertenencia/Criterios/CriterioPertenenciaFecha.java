package metaMapa.hechos.criteriosDePertenencia.Criterios;

import lombok.Data;
import metaMapa.hechos.HechoDeTexto;
import metaMapa.hechos.criteriosDePertenencia.ICriterioDePertenencia;

import java.time.LocalDate;

@Data
public class CriterioPertenenciaFecha implements ICriterioDePertenencia {
  private LocalDate fechaInicio;
  private LocalDate fechaFinal;

  public CriterioPertenenciaFecha(LocalDate fechaInicio, LocalDate fechaFinal) {
    this.fechaInicio = fechaInicio;
    this.fechaFinal = fechaFinal;
  }

  @Override
  public boolean perteneceA(HechoDeTexto hecho) {
    LocalDate fechaHecho = hecho.getFechaHecho();

    return (fechaHecho.isAfter(fechaInicio) && fechaHecho.isBefore(fechaFinal));
  }
}
