package __tpa_mi_no_grupo_22.gestion_usuarios.models.repositories;

import __tpa_mi_no_grupo_22.gestion_usuarios.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
  public Optional<Usuario> findByNombre(String nombre);

  public Optional<Usuario> findByEmail(String email);
}
