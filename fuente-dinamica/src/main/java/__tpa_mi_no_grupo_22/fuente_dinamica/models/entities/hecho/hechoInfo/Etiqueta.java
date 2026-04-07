package __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo;

import lombok.Data;

@Data
public class Etiqueta {

    String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }
}
