package __tpa_mi_no_grupo_22.fuente_dinamica.services.seederService;

import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.Hecho;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Origen;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.hechoInfo.Ubicacion;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.hechoRepository.IHechoRepository;
import __tpa_mi_no_grupo_22.fuente_dinamica.models.repositories.soliEliminacionRepository.ISoliEliminacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SeederService implements ISeederService{
  @Autowired
  private IHechoRepository hechoRepository;
  @Autowired
  private ISoliEliminacionRepository soliEliminacionRepository;
  @Override
  public void init() {
    Hecho hecho1 = Hecho.builder()
        .titulo("Caída de aeronave impacta en Olavarría VENGO DE FUENTE DINAMICA")
        .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
        .categoria(new Categoria("Caída de aeronave"))
        .ubicacion(new Ubicacion(
            new BigDecimal("-36.868375"),
            new BigDecimal("-60.343297")
            )
        )
        .fechaHecho(LocalDateTime.of(2001, 11, 29, 3, 50, 0))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    Hecho hecho2 = Hecho.builder()
        .titulo("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén")
        .descripcion("Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
        .categoria(new Categoria("Accidente con maquinaria industrial"))
        .ubicacion(
            new Ubicacion(
                new BigDecimal("-37.345571"),
                new BigDecimal("-70.241485")
            )
        )
        .fechaHecho(LocalDateTime.of(2001, 8, 16, 10, 0, 0))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    Hecho hecho3 = Hecho.builder()
        .titulo("Caída de aeronave impacta en Venado Tuerto, Santa Fe")
        .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.")
        .categoria(new Categoria("Caída de aeronave"))
        .ubicacion(new Ubicacion(
            new BigDecimal("-33.768051"),
            new BigDecimal("-61.921032")
            )
        )
        .fechaHecho(LocalDateTime.of(2008, 8, 8, 10, 30, 0))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    Hecho hecho4 = Hecho.builder()
        .titulo("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires")
        .descripcion("Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.")
        .categoria(new Categoria("Accidente en paso a nivel"))
        .ubicacion(new Ubicacion(
            new BigDecimal("-35.855811"),
            new BigDecimal("-61.940589")
            )
        )
        .fechaHecho(LocalDateTime.of(2020, 1, 27, 15, 0, 40))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    Hecho hecho5 = Hecho.builder()
        .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
        .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
        .categoria(new Categoria("Derrumbe en obra en construcción"))
        .ubicacion(new Ubicacion(
            new BigDecimal("-26.780008"),
            new BigDecimal("-60.458782")
            )
        )
        .fechaHecho(LocalDateTime.of(2016, 6, 4, 23, 12, 15))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();

    this.hechoRepository.save(hecho1);
    this.hechoRepository.save(hecho2);
    this.hechoRepository.save(hecho3);
    this.hechoRepository.save(hecho4);
    this.hechoRepository.save(hecho5);

    SolicitudEliminacion solicitudEliminacion1 = new SolicitudEliminacion(hecho1, "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");
    SolicitudEliminacion solicitudEliminacion2 = new SolicitudEliminacion(hecho2,
        "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");
    SolicitudEliminacion solicitudEliminacion3 = new SolicitudEliminacion(hecho3,
        "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");

    this.soliEliminacionRepository.save(solicitudEliminacion1);
    this.soliEliminacionRepository.save(solicitudEliminacion2);
    this.soliEliminacionRepository.save(solicitudEliminacion3);
  }
}
