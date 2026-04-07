package metaMapa.hechos;

import lombok.Data;

@Data
public class Etiqueta {

    String descripcion;

    public Etiqueta(String descripcion) {
        this.descripcion = descripcion;
    }
}
