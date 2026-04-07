package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos;

import lombok.Data;

@Data
public class Etiqueta {
    String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }
}
