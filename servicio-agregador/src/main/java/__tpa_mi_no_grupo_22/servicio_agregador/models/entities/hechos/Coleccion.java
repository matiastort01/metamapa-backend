package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", columnDefinition = "VARCHAR(100)")
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "VARCHAR(500)")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // TODO: CONVENIENE SACAR EL CASCADE Y CREAR UN REPO PROPIO PARA LOS CRITERIOS?
    @JoinColumn(name = "coleccion_id", referencedColumnName = "id", nullable = false)
    private List<ACriterioDePertenencia> criterios;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_algoritmo_consenso")
    private TipoAlgoritmoConsenso tipoAlgoritmoConsenso;

    @ManyToMany
    @JoinTable(
        name = "coleccion_fuente",
        joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
    )
    private Set<Fuente> fuentes;

    public void agregarCriterio(ACriterioDePertenencia criterio) {
        if( !this.criterios.contains(criterio) ){
            this.criterios.add(criterio);
        }
    }

    public void eliminarCriterio(String tituloCriterio) {
        ACriterioDePertenencia criterio = this.criterios.stream().filter(c-> c.getNombre().equals(tituloCriterio)).findFirst()
            .orElseThrow(() -> new RuntimeException("Criterio no encontrado: " + tituloCriterio));

        this.criterios.remove(criterio);
    }

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }

    public void eliminarFuente(Fuente fuente) {
        this.fuentes.remove(fuente);
    }
}
