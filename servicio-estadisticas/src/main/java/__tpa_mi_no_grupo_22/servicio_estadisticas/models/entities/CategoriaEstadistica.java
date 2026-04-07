package __tpa_mi_no_grupo_22.servicio_estadisticas.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@Table(name = "categoria_estadistica")
public class CategoriaEstadistica {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String nombre;

  @Column
  private Long cantidad;

  @Column
  private String provincia;
  @Column
  private Integer hora;
  @Column
  private LocalDateTime horaCreacion;

  public CategoriaEstadistica() {
  }

  public CategoriaEstadistica(String nombre, Long cantidad, String provincia, Integer hora) {
    this.nombre = nombre;
    this.cantidad = cantidad;
    this.provincia = provincia;
    this.hora = hora;
    horaCreacion = LocalDateTime.now();
  }

  // getters y setters
}