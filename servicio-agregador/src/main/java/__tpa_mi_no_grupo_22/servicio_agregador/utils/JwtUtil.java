package __tpa_mi_no_grupo_22.servicio_agregador.utils;

import lombok.Getter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtUtil {
  private static final String SECRET = System.getenv("JWT_SECRET") != null
      ? System.getenv("JWT_SECRET")
      : "mi_clave_super_secreta_123456789"; // o usar @Value en Spring

  private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

  public static String validarToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
