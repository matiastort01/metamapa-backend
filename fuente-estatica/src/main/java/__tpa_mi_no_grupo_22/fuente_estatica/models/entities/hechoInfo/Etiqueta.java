package __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo;

import lombok.Data;

@Data
public class Etiqueta {

    String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }
}
