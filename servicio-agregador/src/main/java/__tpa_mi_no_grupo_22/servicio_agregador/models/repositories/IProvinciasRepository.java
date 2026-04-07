package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IProvinciasRepository extends JpaRepository<Provincia, Long> {
  Optional<Provincia> findByNombreIgnoreCase(String nombre);

  @Query("SELECT p FROM Provincia p JOIN p.sinonimos s WHERE LOWER(s) = LOWER(:valor)")
  Optional<Provincia> findBySinonimoIgnoreCase(@Param("valor") String valor);
}
