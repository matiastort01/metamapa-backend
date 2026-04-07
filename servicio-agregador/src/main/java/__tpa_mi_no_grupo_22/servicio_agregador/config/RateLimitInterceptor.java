package __tpa_mi_no_grupo_22.servicio_agregador.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private final Bucket bucket;

  public RateLimitInterceptor() {
    // Configuración: Permite 10 peticiones por minuto
    Bandwidth limit = Bandwidth.classic(500, Refill.greedy(50, Duration.ofMinutes(1)));
    this.bucket = Bucket.builder()
        .addLimit(limit)
        .build();
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // Intenta consumir 1 token del balde
    if (bucket.tryConsume(1)) {
      // Si hay fichas, deja pasar la petición (return true)
      return true;
    } else {
      // Si no hay fichas, bloquea y devuelve error 429
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.getWriter().write("Has excedido el limite de solicitudes. Calmate un poco.");
      return false;
    }
  }
}
