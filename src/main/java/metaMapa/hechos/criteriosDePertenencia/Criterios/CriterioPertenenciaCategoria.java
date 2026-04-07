package metaMapa.hechos.criteriosDePertenencia.Criterios;

import lombok.Data;
import metaMapa.hechos.HechoDeTexto;
import metaMapa.hechos.criteriosDePertenencia.ICriterioDePertenencia;

@Data
public class CriterioPertenenciaCategoria implements ICriterioDePertenencia {
  private String categoria;

  public CriterioPertenenciaCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public boolean perteneceA(HechoDeTexto hecho) {
    return hecho.getCategoria().getNombre().equals(categoria);
  }
}
