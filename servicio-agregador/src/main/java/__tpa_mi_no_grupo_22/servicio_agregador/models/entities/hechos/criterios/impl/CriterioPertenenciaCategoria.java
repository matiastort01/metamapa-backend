package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("CATEGORIA")
@Data
public class CriterioPertenenciaCategoria extends ACriterioDePertenencia {
    @Column(name = "categoria", length = 80)
    private String categoria;

    public CriterioPertenenciaCategoria(String nombreCriterio, String categoria) {
        this.categoria = categoria;
        this.nombre = nombreCriterio;
    }
    // ✅ Constructor por defecto (obligatorio para Hibernate)
    protected CriterioPertenenciaCategoria() {
        super();
    }
    @Override
    public boolean perteneceA(Hecho hecho) {
        return hecho.getCategoria().getNombre().equals(categoria);
    }

}
