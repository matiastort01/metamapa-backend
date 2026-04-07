package __tpa_mi_no_grupo_22.gestion_usuarios.services;

import __tpa_mi_no_grupo_22.gestion_usuarios.dto.AuthResponseDTO;
import __tpa_mi_no_grupo_22.gestion_usuarios.exceptions.NotFoundException;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.Usuario;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Permiso;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.repositories.UsuariosRepository;
import __tpa_mi_no_grupo_22.gestion_usuarios.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {
  private final UsuariosRepository usuariosRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  /**
   * Orquesta el proceso de autenticación y generación de la respuesta.
   * Este es el ÚNICO método público para el login.
   */
  public AuthResponseDTO autenticarYGenerarRespuesta(String email, String password) {
    Usuario usuario = validarCredenciales(email, password);
    List<GrantedAuthority> authorities = recopilarAuthorities(usuario);
    return generarRespuestaConTokens(usuario, authorities);
  }

  /**
   * Valida que el email y la contraseña sean correctos.
   */
  private Usuario validarCredenciales(String email, String password) {
    Usuario usuario = usuariosRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Usuario", email));

    if (!passwordEncoder.matches(password, usuario.getContrasena())) {
      throw new NotFoundException("Usuario", email);
    }
    return usuario;
  }

  /**
   * MÉTODO CORREGIDO: Recopila el ROL único y los PERMISOS del usuario.
   */
  private List<GrantedAuthority> recopilarAuthorities(Usuario usuario) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    // 1. Añadir el rol único, asegurándose de que tenga el prefijo ROLE_
    if (usuario.getRol() != null) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }

    // 2. Añadir todos los permisos individuales
    if (usuario.getPermisos() != null) {
      for (Permiso permiso : usuario.getPermisos()) {
        authorities.add(new SimpleGrantedAuthority(permiso.name()));
      }
    }
    return authorities;
  }

  /**
   * Genera los tokens y construye el DTO de respuesta final.
   */
  private AuthResponseDTO generarRespuestaConTokens(Usuario usuario, List<GrantedAuthority> authorities) {
    String accessToken = JwtUtil.generarAccessToken(usuario.getEmail(), authorities);
    String refreshToken = JwtUtil.generarRefreshToken(usuario.getEmail());

    List<String> rolesYPermisosParaDto = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return AuthResponseDTO.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .roles(rolesYPermisosParaDto) // El campo 'roles' ahora contiene el rol + los permisos
        .build();
  }
}

