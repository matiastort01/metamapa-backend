package metaMapa.hechos;

import metaMapa.hechos.criteriosDePertenencia.ICriterioDePertenencia;
import metaMapa.utils.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColeccionTest {

  private Coleccion coleccion;
  private HechoDeTexto hechoValido;
  private HechoDeTexto hechoInvalido;

  @BeforeEach
  void setUp() {
    // Criterio: solo hechos cuya categoría tenga nombre "SOCIAL"
    ICriterioDePertenencia criterio = hecho -> hecho.getCategoria().getNombre().equals("SOCIAL");

    coleccion = new Coleccion("Título", "Descripción", criterio);

    hechoValido = HechoDeTexto.builder()
        .titulo("Hecho A")
        .descripcion("Descripcion A")
        .categoria(new Categoria("SOCIAL"))
        .ubicacion(new Ubicacion(10.50, 9.50))
        .origen(Origen.CargaManual)
        .fechaDeCarga(LocalDateTime.now())
        .build();

    hechoInvalido = HechoDeTexto.builder()
        .titulo("Hecho B")
        .descripcion("Descripcion B")
        .categoria(new Categoria("POLITICA"))
        .ubicacion(new Ubicacion(-10.50, -2.00))
        .origen(Origen.CargaManual)
        .fechaDeCarga(LocalDateTime.now())
        .build();
  }

  @Test
  void testAgregaHechoQueCumpleCriterio() {
    coleccion.agregarHechos(hechoValido);

    List<HechoDeTexto> hechos = coleccion.getHechos();
    assertEquals(1, hechos.size());
    assertEquals("Hecho A", hechos.get(0).getTitulo());
  }

  @Test
  void testNoAgregaHechoQueNoCumpleCriterio() {
    coleccion.agregarHechos(hechoInvalido);

    assertFalse(coleccion.getHechos().contains(hechoInvalido));
  }

  @Test
  void testNoAgregaHechoDuplicado() {
    HechoDeTexto hechoConMismoTitulo = HechoDeTexto.builder()
        .titulo("Hecho A")
        .descripcion("Otra descripcion")
        .categoria(new Categoria("SOCIAL"))
        .ubicacion(new Ubicacion(5.00, -5.00))
        .origen(Origen.CargaManual)
        .fechaDeCarga(LocalDateTime.now())
        .build();

    coleccion.agregarHechos(hechoValido);
    coleccion.agregarHechos(hechoConMismoTitulo); // mismo título → duplicado

    List<HechoDeTexto> hechos = coleccion.getHechos();
    assertEquals(1, hechos.size());
  }

  @Test
  void testIgnoraHechoNull() {
    coleccion.agregarHechos((HechoDeTexto) null);

    assertTrue(coleccion.getHechos().isEmpty());
  }
}
