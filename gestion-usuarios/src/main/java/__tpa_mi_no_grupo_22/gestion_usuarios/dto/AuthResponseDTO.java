package __tpa_mi_no_grupo_22.gestion_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
  private List<String> roles;
}
