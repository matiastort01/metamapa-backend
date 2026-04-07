package __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities;


import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@Entity
@Table(name = "provincia_coleccion")
public class ProvinciaColeccionEstadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String titulo;
    @Column
    String provincia;

    @Column
    Long cantidad;

    @Column
    LocalDateTime fechaCreacion;

    public ProvinciaColeccionEstadistica(String titulo, String provincia, Long cantidad) {
        this.titulo = titulo;
        this.provincia = provincia;
        this.cantidad = cantidad;
        this.fechaCreacion = LocalDateTime.now();
    }

}
