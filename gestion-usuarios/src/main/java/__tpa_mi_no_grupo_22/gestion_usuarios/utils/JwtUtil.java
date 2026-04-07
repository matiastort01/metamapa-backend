package __tpa_mi_no_grupo_22.gestion_usuarios.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUtil {

  private static final String SECRET = System.getenv("JWT_SECRET") != null
      ? System.getenv("JWT_SECRET")
      : "mi_clave_super_secreta_123456789";

  @Getter
  private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

  private static final long ACCESS_TOKEN_VALIDITY = 240 * 60 * 1000; // 15 min
  private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

  /**
   * Genera un Access Token que incluye los roles del usuario como un "claim".
   * @param username El email del usuario.
   * @param authorities La colección de roles y permisos.
   * @return El String del JWT.
   */
  public static String generarAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
    // 1. Convertimos los roles a una lista de Strings
    List<String> roles = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    // 2. Añadimos los roles como un "claim" al token
    return Jwts.builder()
        .setSubject(username)
        .claim("roles", roles) // <-- ¡AQUÍ ESTÁ LA CLAVE!
        .setIssuer("gestion-usuarios")
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
        .signWith(key)
        .compact();
  }

  public static String generarRefreshToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuer("gestion-usuarios")
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
        .claim("type", "refresh")
        .signWith(key)
        .compact();
  }

  public static String validarToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}

