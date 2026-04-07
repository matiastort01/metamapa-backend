package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Departamento;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Municipio;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Provincia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ubicacion",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"latitud", "longitud"})
    }
)
@Builder
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitud",  nullable = false, precision = 12, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 12, scale = 7)
    private BigDecimal longitud;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    public Ubicacion(BigDecimal latitud, BigDecimal longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
