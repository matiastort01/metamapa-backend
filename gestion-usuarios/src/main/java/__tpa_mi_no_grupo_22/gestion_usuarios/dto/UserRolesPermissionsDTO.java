package __tpa_mi_no_grupo_22.gestion_usuarios.dto;

import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Permiso;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesPermissionsDTO {
  private String username;
  private Rol rol;
  private List<Permiso> permisos;
}

