package __tpa_mi_no_grupo_22.fuente_estatica.models.repositories;

import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
//TODO: ROLLBACKEAR EL REPO( NO HACE FALTA LA BASE DE DATOS) :P
public interface IHechoRepository extends JpaRepository<Hecho, Long> {
}
