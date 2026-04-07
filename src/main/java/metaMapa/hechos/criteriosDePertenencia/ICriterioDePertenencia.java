package metaMapa.hechos.criteriosDePertenencia;

import metaMapa.hechos.HechoDeTexto;

/**
 * Esta interfaz servirá para que los futuros criterios de pertenencia (clases)
 * que desarrollaremos más adelante la implementen.
 */
public interface ICriterioDePertenencia {

  boolean perteneceA(HechoDeTexto hecho);
}
