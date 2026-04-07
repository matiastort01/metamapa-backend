package __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
  Optional<Categoria> findByNombre(String nombre);
}
