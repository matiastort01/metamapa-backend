package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "provincia")
@Builder
public class Provincia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String nombre;
  @ElementCollection
  @CollectionTable(
      name = "provincia_sinonimo",
      joinColumns = @JoinColumn(name = "provincia_id")
  )
  @Column(name = "sinonimo")
  private List<String> sinonimos;

  public Provincia(String nombre) {
    this.nombre = nombre;
  }
}
