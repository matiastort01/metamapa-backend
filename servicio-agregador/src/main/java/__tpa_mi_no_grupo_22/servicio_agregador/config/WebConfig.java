package __tpa_mi_no_grupo_22.servicio_agregador.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private RateLimitInterceptor rateLimitInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // Aquí registras el interceptor
    registry.addInterceptor(rateLimitInterceptor)
        .addPathPatterns("/**"); // <--- ESTO SIGNIFICA "INTERCEPTA TODO"

    // Si quisieras que solo aplique a la API y no al login, harías:
    // .addPathPatterns("/api/**");
  }
}
