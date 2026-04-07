package __tpa_mi_no_grupo_22.gestion_usuarios.controllers;

import __tpa_mi_no_grupo_22.gestion_usuarios.dto.AuthResponseDTO;
import __tpa_mi_no_grupo_22.gestion_usuarios.dto.RefreshRequest;
import __tpa_mi_no_grupo_22.gestion_usuarios.dto.TokenResponse;
import __tpa_mi_no_grupo_22.gestion_usuarios.dto.UserRolesPermissionsDTO;
import __tpa_mi_no_grupo_22.gestion_usuarios.dto.UsuarioDTO;
import __tpa_mi_no_grupo_22.gestion_usuarios.exceptions.NotFoundException;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.Usuario;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Permiso;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Rol;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.repositories.UsuariosRepository;
import __tpa_mi_no_grupo_22.gestion_usuarios.services.LoginService;
import __tpa_mi_no_grupo_22.gestion_usuarios.services.RegisterService;
import __tpa_mi_no_grupo_22.gestion_usuarios.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  private final LoginService loginService;
  private final RegisterService registerService;
  private final UserDetailsService userDetailsService; // Inyectado para el refresh token

  @Autowired
  private UsuariosRepository usuariosRepository;

  @PostMapping
  public ResponseEntity<AuthResponseDTO> loginApi(@RequestBody Map<String, String> credentials) {
    try {
      log.info("Recibida petición de login para: {}", credentials.get("email"));
      String email = credentials.get("email");
      String password = credentials.get("password");

      if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      // LLAMADA AL SERVICIO login
      AuthResponseDTO response = loginService.autenticarYGenerarRespuesta(email, password);

      log.info("Usuario {} logueado exitosamente.", email);
      return ResponseEntity.ok(response);

    } catch (NotFoundException e) {
      log.warn("Intento de login fallido para el usuario: {}", credentials.get("email"));
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 es más apropiado para credenciales incorrectas
    } catch (Exception e) {
      log.error("Error inesperado en loginApi", e);
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
    try {
      String username = JwtUtil.validarToken(request.getRefreshToken());

      // Validar que es un refresh token
      Claims claims = Jwts.parserBuilder().setSigningKey(JwtUtil.getKey()).build().parseClaimsJws(request.getRefreshToken()).getBody();
      if (!"refresh".equals(claims.get("type"))) {
        return ResponseEntity.badRequest().build();
      }

      // Cargar los detalles del usuario para obtener sus roles y generar un nuevo access token
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      String newAccessToken = JwtUtil.generarAccessToken(username, userDetails.getAuthorities());

      TokenResponse response = new TokenResponse(newAccessToken, request.getRefreshToken());
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      log.error("Error al refrescar el token", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/user/roles-permisos")
  public ResponseEntity<UserRolesPermissionsDTO> getUserRolesAndPermission(
      @RequestHeader("Authorization") String authorizationHeader) {

    // 🔹 Extraer el token del header "Bearer <token>"
    String token = authorizationHeader.replace("Bearer ", "").trim();

    // 🔹 Obtener el email (subject) validando el JWT
    String email;
    try {
      email = JwtUtil.validarToken(token); // ← esto te devuelve el subject del token (el email)
    } catch (ExpiredJwtException e) {
      // Si querés refrescar automáticamente, podés hacerlo acá
      log.warn("Access token expirado para {}", e.getClaims().getSubject());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (JwtException e) {
      log.error("Token inválido: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 🔹 Buscar el usuario
    Optional<Usuario> usuarioOptional = usuariosRepository.findByEmail(email);
    if (usuarioOptional.isEmpty()) {
      throw new NotFoundException("Usuario", email);
    }

    Usuario usuario = usuarioOptional.get();

    // 🔹 Construir el DTO con sus roles y permisos
    UserRolesPermissionsDTO response = UserRolesPermissionsDTO.builder()
        .username(email)
        .rol(usuario.getRol())
        .permisos(usuario.getPermisos())
        .build();

    return ResponseEntity.ok(response);
  }


  @PostMapping("/register")
  public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
    try {
      Usuario usuarioCreado = registerService.registraUsuario(usuarioDTO);

      UsuarioDTO response = UsuarioDTO.builder()
          .id(usuarioCreado.getId())
          .nombre(usuarioCreado.getNombre())
          .email(usuarioCreado.getEmail())
          .build();

      log.info("Nuevo usuario registrado: {}", usuarioCreado.getEmail());
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (RuntimeException e) {
      log.error("Error al registrar usuario: {}", e.getMessage());
      if (e.getMessage().contains("registrado")) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      log.error("Error inesperado en el registro", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}