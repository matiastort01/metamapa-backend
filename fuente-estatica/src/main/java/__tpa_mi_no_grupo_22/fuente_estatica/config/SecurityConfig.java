//package __tpa_mi_no_grupo_22.fuente_estatica.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http
//        // 1. Deshabilitar CSRF (Fundamental para APIs y subida de archivos desde otro servicio)
//        .csrf(AbstractHttpConfigurer::disable)
//
//        // 2. Deshabilitar el Login por defecto
//        .httpBasic(AbstractHttpConfigurer::disable)
//        .formLogin(AbstractHttpConfigurer::disable)
//
//        .authorizeHttpRequests(auth -> auth
//            // ✅ PERMITIR IMPORTACIÓN (POST)
//            .requestMatchers(HttpMethod.POST, "/fuente-estatica/hechos/importar").permitAll()
//
//            // ✅ PERMITIR CONSULTAS (GET)
//            .requestMatchers(HttpMethod.GET, "/fuente-estatica/hechos/**").permitAll()
//
//            // 🔒 Bloquear el resto por precaución
//            .anyRequest().authenticated()
//        );
//
//    return http.build();
//  }
//}