package escenariosEntrega1;

import metaMapa.hechos.Categoria;
import metaMapa.hechos.Coleccion;
import metaMapa.hechos.HechoDeTexto;
import metaMapa.hechos.Origen;
import metaMapa.hechos.criteriosDePertenencia.Criterios.CriterioPertenenciaTitulo;
import metaMapa.hechos.solicitudEliminacion.EstadoSolicitud;
import metaMapa.hechos.solicitudEliminacion.SolicitudEliminacion;
import metaMapa.usuario.Administrador;
import metaMapa.usuario.Contribuyente;
import metaMapa.utils.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class Escenario3 {
  @Test
  void solicitudConJustificacionDeMenosDe500Caracteres(){
    // El Contribuyente crea una solicitud con una justificacion de menos de 500 caracteres

    // Un contribuyente aporta un hecho
    HechoDeTexto hecho = HechoDeTexto.builder()
        .titulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")
        .descripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.")
        .categoria(new Categoria("Evento sanitario"))
        .ubicacion(new Ubicacion(-32.786098, -60.741543))
        .fechaHecho(LocalDate.of(2005, 7, 5))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();

    // Un contribuyente solicita la eliminacion de un hecho con una justificacion que tiene menos de 500 caracteres
    Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, "Justificacion");
    });

    assertEquals("La justificación debe tener al menos 500 caracteres.", excepcion.getMessage());
  }

  @Test
  void administradorRechazaSolicitudHechoPuedeSerAgregadoAColeccion(){
    // Un administrador rechaza la solicitud, por ende el hecho puede ser agregado a una coleccion

    // Un contribuyente aporta un hecho
    HechoDeTexto hecho = HechoDeTexto.builder()
        .titulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")
        .descripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.")
        .categoria(new Categoria("Evento sanitario"))
        .ubicacion(new Ubicacion(-32.786098, -60.741543))
        .fechaHecho(LocalDate.of(2005, 7, 5))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();

    // Un contribuyente solicita la eliminacion del hecho
    SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(hecho, "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");

    // Un administrador rechaza la solicitud
    solicitudEliminacion.serRechazada();

    // El hecho puede ser añadido a una coleccion
    Coleccion coleccion = new Coleccion("titulo", "descripcion", new CriterioPertenenciaTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe"), hecho);

    assertTrue(coleccion.getHechos().contains(hecho));
  }

  @Test
  void administradorRechazaSolicitudYQuedaEnEstadoRechazada(){
    // Un administrador rechaza la solicitud, por ende la solicitud queda en estado RECHAZADA

    // Un contribuyente aporta un hecho
    HechoDeTexto hecho = HechoDeTexto.builder()
        .titulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")
        .descripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.")
        .categoria(new Categoria("Evento sanitario"))
        .ubicacion(new Ubicacion(-32.786098, -60.741543))
        .fechaHecho(LocalDate.of(2005, 7, 5))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();

    // Un contribuyente solicita la eliminacion del hecho
    SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(hecho, "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");

    // Un administrador rechaza la solicitud
    solicitudEliminacion.serRechazada();

    assertEquals(solicitudEliminacion.getEstadoSolicitud(), EstadoSolicitud.RECHAZADA);
  }

  @Test
  void administradorAceptaSolicitudHechoNoPuedeSerAgregadoAColeccion(){
    // Un administrador acepta la solicitud, por ende el hecho no puede ser agregado a una coleccion

    // Un contribuyente aporta un hecho
    HechoDeTexto hecho = HechoDeTexto.builder()
        .titulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")
        .descripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.")
        .categoria(new Categoria("Evento sanitario"))
        .ubicacion(new Ubicacion(-32.786098, -60.741543))
        .fechaHecho(LocalDate.of(2005, 7, 5))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();

    // Un contribuyente solicita la eliminacion del hecho
    SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(hecho, "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");

    // Un administrador rechaza la solicitud
    solicitudEliminacion.serAceptada();

    // El hecho NO puede ser añadido a una coleccion
    Coleccion coleccion = new Coleccion("titulo", "descripcion", new CriterioPertenenciaTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe"), hecho);

    assertFalse(coleccion.getHechos().contains(hecho));
  }

  @Test
  void administradorAceptaSolicitudYQuedaEnEstadoAceptada(){
    // Un administrador acepta la solicitud, por ende la solicitud queda en estado ACEPTADA

    // Un contribuyente aporta un hecho
    HechoDeTexto hecho = HechoDeTexto.builder()
        .titulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")
        .descripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.")
        .categoria(new Categoria("Evento sanitario"))
        .ubicacion(new Ubicacion(-32.786098, -60.741543))
        .fechaHecho(LocalDate.of(2005, 7, 5))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();

    // Un contribuyente solicita la eliminacion del hecho
    SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(hecho, "La publicación sobre el brote de enfermedad contagiosa en San Lorenzo, Santa Fe, debería ser eliminada debido a que carece de fuentes verificables y podría generar alarma social innecesaria en la población. La información difundida no ha sido confirmada por autoridades sanitarias oficiales y podría contribuir a la desinformación, afectando la imagen de la localidad y perjudicando tanto a residentes como a visitantes. Mantener información no verificada o imprecisa puede dañar la credibilidad del sitio y alimentar rumores infundados. Por estas razones, se solicita su remoción inmediata hasta tanto se disponga de datos confirmados por organismos competentes.");

    // Un administrador acepta la solicitud
    solicitudEliminacion.serAceptada();

    assertEquals(solicitudEliminacion.getEstadoSolicitud(), EstadoSolicitud.ACEPTADA);
  }
}
