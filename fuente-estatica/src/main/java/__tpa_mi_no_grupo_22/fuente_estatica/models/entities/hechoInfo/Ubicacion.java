package __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ubicacion")
@Builder
public class Ubicacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "latitud", columnDefinition = "DECIMAL(12,2)")
  private Double latitud;
  @Column(name = "longitud", columnDefinition = "DECIMAL(12,2)")
  private Double longitud;
}
