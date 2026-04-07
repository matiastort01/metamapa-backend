package __tpa_mi_no_grupo_22.gestion_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
  private Long id;
  private String nombre;
  private String email;
  private String contrasena;
}
