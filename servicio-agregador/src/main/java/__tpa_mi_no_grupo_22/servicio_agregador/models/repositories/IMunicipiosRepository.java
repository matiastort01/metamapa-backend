package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IMunicipiosRepository extends JpaRepository<Municipio, Long> {
  Optional<Municipio> findByNombreIgnoreCase(String nombre);

  @Query("SELECT m FROM Municipio m JOIN m.sinonimos s WHERE LOWER(s) = LOWER(:valor)")
  Optional<Municipio> findBySinonimoIgnoreCase(@Param("valor") String valor);
}
