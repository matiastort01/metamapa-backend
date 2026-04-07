package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "estado_consenso", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"titulo_hecho", "titulo_coleccion", "tipo_algoritmo_consenso"})
})
public class EstadoConsenso {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo_hecho")
  private String tituloHecho;

  @Column(name = "titulo_coleccion")
  private String tituloColeccion;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_algoritmo_consenso")
  private TipoAlgoritmoConsenso tipoAlgoritmoConsenso;

  @Column(name = "consensuado")
  private boolean consensuado;
}
