package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria")
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;

    @ElementCollection
    @CollectionTable(
        name = "categoria_sinonimo", // nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "categoria_id") // FK hacia Categoria
    )
    @Column(name = "sinonimo")
    private List<String> sinonimos;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public void addSinonimo(String sinonimo){
        this.sinonimos.add(sinonimo);
    }
}
