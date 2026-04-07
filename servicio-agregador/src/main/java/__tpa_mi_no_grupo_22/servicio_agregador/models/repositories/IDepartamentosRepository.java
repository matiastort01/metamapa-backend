package __tpa_mi_no_grupo_22.servicio_agregador.models.repositories;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.ubicacion.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IDepartamentosRepository extends JpaRepository<Departamento, Long> {
  Optional<Departamento> findByNombreIgnoreCase(String nombre);

  @Query("SELECT d FROM Departamento d JOIN d.sinonimos s WHERE LOWER(s) = LOWER(:valor)")
  Optional<Departamento> findBySinonimoIgnoreCase(@Param("valor") String valor);
}
