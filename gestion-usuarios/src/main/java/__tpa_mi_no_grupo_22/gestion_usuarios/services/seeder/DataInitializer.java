package __tpa_mi_no_grupo_22.gestion_usuarios.services.seeder;

import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.Usuario;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Permiso;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Rol;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.repositories.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner { // CommandLineRunner le dice a Spring Boot que ejecute el método run() tan pronto como la aplicación arranque.

  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
  private final UsuariosRepository usuariosRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    crearAdminSiNoExiste();
  }

  private void crearAdminSiNoExiste() {
    String adminEmail = "admin@metamapa.com";

    // Verifico si el usuario administrador ya existe
    if (usuariosRepository.findByEmail(adminEmail).isEmpty()) {

      // Creo el usuario con el ROL.ADMIN
      Usuario admin = Usuario.builder()
          .nombre("admin")
          .email(adminEmail)
          .contrasena(passwordEncoder.encode("4321")) // Contraseña: 4321
          .rol(Rol.ADMIN)
          .permisos(new ArrayList<>())
          .build();

      // AsignO todos los permisos que necesite
      admin.agregarPermiso(Permiso.SOLICITAR_ELIMINACION);
      admin.agregarPermiso(Permiso.SUBIR_HECHOS);
      admin.agregarPermiso(Permiso.EDITAR_HECHOS);
      admin.agregarPermiso(Permiso.PANEL_CONTROL);
      admin.agregarPermiso(Permiso.MANIPULAR_COLECCIONES);
      admin.agregarPermiso(Permiso.CONFIGURAR_FUENTES);
      admin.agregarPermiso(Permiso.CONFIGURAR_ALGORITMO_CONSENSO);
      admin.agregarPermiso(Permiso.GESTIONAR_SOLICITUD_ELIM);
      admin.agregarPermiso(Permiso.IMPORTAR_CSV);

      usuariosRepository.save(admin);
      log.info(">>> Usuario administrador creado exitosamente: {} <<<", adminEmail);
    } else {
      log.info(">>> El usuario administrador ya existe. No se realizaron cambios. <<<");
    }
  }
}
