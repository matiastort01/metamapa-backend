package __tpa_mi_no_grupo_22.gestion_usuarios.services;

import __tpa_mi_no_grupo_22.gestion_usuarios.dto.UsuarioDTO;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.Usuario;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Permiso;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.usuarios.Rol;
import __tpa_mi_no_grupo_22.gestion_usuarios.models.repositories.UsuariosRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RegisterService {
  private final UsuariosRepository usuariosRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public RegisterService(UsuariosRepository usuariosRepository, BCryptPasswordEncoder passwordEncoder) {
    this.usuariosRepository = usuariosRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Usuario registraUsuario(UsuarioDTO dto){
    //Guarda con los duplicados que la pija no es de trapo 🥵
    if(usuariosRepository.findByEmail(dto.getEmail()).isPresent()){
      throw new RuntimeException("el email ya esta registrado");
    }

    Usuario usuario = Usuario.builder()
        .nombre(dto.getNombre())
        .email(dto.getEmail())
        .contrasena(passwordEncoder.encode(dto.getContrasena()))
        .rol((dto.getNombre() == "admin") ? Rol.ADMIN:  Rol.CONTRIBUYENTE)
        .permisos(new ArrayList<>())
        .build();

    usuario.agregarPermiso(Permiso.SOLICITAR_ELIMINACION);
    usuario.agregarPermiso(Permiso.SUBIR_HECHOS);
    usuario.agregarPermiso(Permiso.EDITAR_HECHOS);

    return usuariosRepository.save(usuario);
  }


}
