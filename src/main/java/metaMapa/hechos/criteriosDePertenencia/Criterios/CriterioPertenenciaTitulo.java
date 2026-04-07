package metaMapa.hechos.criteriosDePertenencia.Criterios;

import metaMapa.hechos.HechoDeTexto;
import metaMapa.hechos.criteriosDePertenencia.ICriterioDePertenencia;

public class CriterioPertenenciaTitulo implements ICriterioDePertenencia {
  String titulo;

  public CriterioPertenenciaTitulo(String titulo) {
    this.titulo = titulo;
  }

  @Override
  public boolean perteneceA(HechoDeTexto hecho) {
    return hecho.getTitulo().equals(titulo);
  }
}
