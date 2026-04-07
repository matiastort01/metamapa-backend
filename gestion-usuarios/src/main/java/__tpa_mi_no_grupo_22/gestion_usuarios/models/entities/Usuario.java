package __tpa_mi_no_grupo_22.gestion_usuarios.models.entities;

import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Permiso;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Rol;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nombre;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "contrasena", nullable = false)
  private String contrasena;

  // 👇 Rol como un solo enum (ej: ADMIN, CONTRIBUYENTE, VISUALIZADOR)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Rol rol;

  // 👇 Colección de permisos (enum) mapeada en tabla intermedia
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "usuario_permisos",
      joinColumns = @JoinColumn(name = "usuario_id")
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "permiso")
  private List<Permiso> permisos;

  public void agregarPermiso(Permiso permiso) {
    this.permisos.add(permiso);
  }
}
