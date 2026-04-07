package __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "ubicacion",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"latitud", "longitud"})
    }
)
public class Ubicacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "latitud",  nullable = false, precision = 12, scale = 7)
  private BigDecimal latitud;

  @Column(name = "longitud", nullable = false, precision = 12, scale = 7)
  private BigDecimal longitud;

  public Ubicacion(BigDecimal latitud, BigDecimal longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }
}
