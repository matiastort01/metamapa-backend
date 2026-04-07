package __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.hechoRepository;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
  Optional<Hecho> findByTituloIgnoreCase(String titulo);
  boolean existsByTitulo(String titulo);
  boolean existsByTituloAndIdNot(String titulo, Long id);
}
