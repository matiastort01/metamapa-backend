package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.solicitudes;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud_eliminacion")
@Builder
public class SolicitudEliminacion {
    // ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private Hecho hecho;

    @Column(name = "justificacion", nullable = false, columnDefinition = "VARCHAR(2000)")
    private String justificacion;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud")
    private EstadoSolicitud estadoSolicitud;
}
