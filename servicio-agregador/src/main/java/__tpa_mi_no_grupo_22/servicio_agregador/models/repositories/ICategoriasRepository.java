package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ICategoriasRepository extends JpaRepository<Categoria, Long> {
  Optional<Categoria> findByNombreIgnoreCase(String nombre);

  @Query("SELECT c FROM Categoria c JOIN c.sinonimos s WHERE LOWER(s) = LOWER(:valor)")
  Optional<Categoria> findBySinonimoIgnoreCase(@Param("valor") String valor);

}
