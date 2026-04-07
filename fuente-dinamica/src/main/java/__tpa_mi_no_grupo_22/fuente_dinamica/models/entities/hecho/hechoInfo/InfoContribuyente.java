package __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contribuyente")
@Builder
public class InfoContribuyente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre", nullable = false, columnDefinition = "VARCHAR(50)")
  private String nombre;

  @Column(name = "apellido", columnDefinition = "VARCHAR(50)")
  private String apellido;

  @Column(name = "edad", columnDefinition = "INTEGER")
  private Integer edad;

  public InfoContribuyente(String nombre, String apellido, Integer edad) {
    /*if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre es obligatorio si se proporcionan datos personales.");
    }*/
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
  }
}