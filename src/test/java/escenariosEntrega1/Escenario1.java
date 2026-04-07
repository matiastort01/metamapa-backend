package escenariosEntrega1;

import metaMapa.hechos.Categoria;
import metaMapa.hechos.Coleccion;
import metaMapa.hechos.Etiqueta;
import metaMapa.hechos.HechoDeTexto;
import metaMapa.hechos.Origen;
import metaMapa.hechos.criteriosDePertenencia.Criterios.CriterioPertenenciaCategoria;
import metaMapa.hechos.criteriosDePertenencia.Criterios.CriterioPertenenciaFecha;
import metaMapa.hechos.criteriosDePertenencia.Criterios.CriterioPertenenciaTitulo;
import metaMapa.usuario.Administrador;
import metaMapa.utils.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Escenario1 {
  Administrador administrador;
  HechoDeTexto hecho1;
  HechoDeTexto hecho2;
  HechoDeTexto hecho3;
  HechoDeTexto hecho4;
  HechoDeTexto hecho5;
  Coleccion coleccion;

  @BeforeEach
  void setUp() {
    administrador = new Administrador();
    hecho1 = HechoDeTexto.builder()
        .titulo("Caída de aeronave impacta en Olavarría")
        .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
        .categoria(new Categoria("Caída de aeronave"))
        .ubicacion(new Ubicacion(-36.868375, -60.343297))
        .fechaHecho(LocalDate.of(2001, 11, 29))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    hecho2 = HechoDeTexto.builder()
        .titulo("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén")
        .descripcion("Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
        .categoria(new Categoria("Accidente con maquinaria industrial"))
        .ubicacion(new Ubicacion(-37.345571, -70.241485))
        .fechaHecho(LocalDate.of(2001, 8, 16))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    hecho3 = HechoDeTexto.builder()
        .titulo("Caída de aeronave impacta en Venado Tuerto, Santa Fe")
        .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.")
        .categoria(new Categoria("Caída de aeronave"))
        .ubicacion(new Ubicacion(-33.768051, -61.921032))
        .fechaHecho(LocalDate.of(2008, 8, 8))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    hecho4 = HechoDeTexto.builder()
        .titulo("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires")
        .descripcion("Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.")
        .categoria(new Categoria("Accidente en paso a nivel"))
        .ubicacion(new Ubicacion(-35.855811, -61.940589))
        .fechaHecho(LocalDate.of(2020, 1, 27))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    hecho5 = HechoDeTexto.builder()
        .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
        .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
        .categoria(new Categoria("Derrumbe en obra en construcción"))
        .ubicacion(new Ubicacion(-26.780008, -60.458782))
        .fechaHecho(LocalDate.of(2016, 6, 4))
        .fechaDeCarga(LocalDateTime.now())
        .origen(Origen.Contribuyente)
        .build();
    coleccion = new Coleccion("Coleccion de prueba", "Esto es una prueba", null, hecho1, hecho2, hecho3, hecho4, hecho5);
  }

  @Test
  void escenario1_1() {
    List<HechoDeTexto> hechosEsperados = List.of(hecho1, hecho2, hecho3, hecho4, hecho5);
    assertEquals(coleccion.getHechos(), hechosEsperados);
  }

  @Test
  void escenario1_2() {
    CriterioPertenenciaFecha criterioPertenenciaFecha = new CriterioPertenenciaFecha(LocalDate.of(2000, 1, 1), LocalDate.of(2010, 1, 1));

    coleccion.setCriterio(criterioPertenenciaFecha);
    //Parte 1
    assertTrue(coleccion.getHechos().contains(hecho1) && coleccion.getHechos().contains(hecho2) && coleccion.getHechos().contains(hecho3) && (coleccion.getHechos().stream().count() == 3) && !coleccion.getHechos().contains(hecho4));

    //Parte 2
    CriterioPertenenciaCategoria criterioPertenenciaCategoria = new CriterioPertenenciaCategoria("Caída de aeronave");

    coleccion.setCriterio(criterioPertenenciaCategoria);

    assertTrue(coleccion.getHechos().contains(hecho1) && !coleccion.getHechos().contains(hecho2) && coleccion.getHechos().contains(hecho3) && (coleccion.getHechos().stream().count() == 2));

  }

  @Test
  void escenario1_3() {
    /*Escenario 1.3: Filtros del visualizador
    Sobre la colección aplicar un filtro de tipo categoría = “Caída de Aeronave” y título =”un título”.
    Demostrar que ningún hecho de la colección cumple con este filtro.*/

    CriterioPertenenciaCategoria criterioPertenenciaCategoria = new CriterioPertenenciaCategoria("Caída de aeronave");
    CriterioPertenenciaTitulo criterioTituloPrueba = new CriterioPertenenciaTitulo("un titulo");

    assertFalse(coleccion.getHechos().stream().anyMatch(hecho -> criterioTituloPrueba.perteneceA(hecho) && criterioPertenenciaCategoria.perteneceA(hecho)));
  }

  @Test
  void escenario1_4() {
    /*Etiquetar al hecho titulado “Caída de aeronave impacta en Olavarría” como “Olavarría” .
      Etiquetar al mismo hecho como “Grave”.
      Verificar que el hecho retenga las 2 etiquetas correspondientes.
    */

    Etiqueta etiquetaOlavarria = new Etiqueta("Olavarría");
    Etiqueta etiquetaGrave = new Etiqueta("Grave");
    hecho1.agregarEtiqueta(etiquetaOlavarria);
    hecho1.agregarEtiqueta(etiquetaGrave);
    assertTrue(hecho1.getEtiquetas().contains(etiquetaOlavarria) && hecho1.getEtiquetas().contains(etiquetaGrave));
  }
}






