package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Categoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Coleccion;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.consensos.TipoAlgoritmoConsenso;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaCategoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaFecha;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.ICategoriasRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IColeccionesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class SeederService implements ISeederService {

    @Autowired
    private IColeccionesRepository repoColecciones;

    @Autowired
    private ICategoriasRepository repoCategoria;

    @Override
    public void init() {
//        crearCategorias();
//        crearColeccionesDePrueba();
//        crearColeccionEstafa();
    }

//    private void crearColeccionEstafa() {
//        Categoria c1 = repoCategoria.findByNombreIgnoreCase("Estafa")
//            .orElseThrow(() -> new RuntimeException("Categoría no encontrada: Estafa"));
//
//        Coleccion coleccion1 = new Coleccion(
//            "Estafas Generales",
//            "Colección de hechos relacionados a estafas generales."
//        );
//        coleccion1.setTipoAlgoritmoConsenso(TipoAlgoritmoConsenso.MAYORIA_SIMPLE);
//        coleccion1.agregarFuente("FuenteDinamica::FD-1");
//        coleccion1.agregarFuente("FuenteEstatica::FE-1");
//        coleccion1 = repoColecciones.save(coleccion1);
//
//        // Definición del criterio de fecha que faltaba
//        CriterioPertenenciaFecha criterioFecha1 = new CriterioPertenenciaFecha(
//            "Estafas Recientes",
//            LocalDate.of(2023, 1, 1),
//            LocalDate.of(2025, 12, 31)
//        );
//        criterioFecha1.setColeccion(coleccion1);
//
//        CriterioPertenenciaCategoria criterioCat1 = new CriterioPertenenciaCategoria(
//            "Categoria Estafa",
//            c1.getNombre()
//        );
//        criterioCat1.setColeccion(coleccion1);
//
//        coleccion1.agregarCriterio(criterioFecha1);
//        coleccion1.agregarCriterio(criterioCat1);
//        repoColecciones.save(coleccion1);
//    }
//
//    private void crearCategorias() {
//        List<Categoria> categorias = Arrays.asList(
//            Categoria.builder().nombre("Caída de aeronave").sinonimos(Arrays.asList("accidente aéreo", "choque de avión")).build(),
//            Categoria.builder().nombre("Precipitación de granizo").sinonimos(Arrays.asList("granizada", "tormenta de granizo")).build(),
//            Categoria.builder().nombre("Incidente de aviación").sinonimos(Arrays.asList("incidente aéreo", "problema de aviación")).build(),
//            Categoria.builder().nombre("Vientos fuertes").sinonimos(Arrays.asList("vendaval", "rachas fuertes")).build(),
//            Categoria.builder().nombre("Estafa").sinonimos(Arrays.asList("fraude", "engaño")).build() // Categoría añadida
//        );
//
//        for (Categoria categoria : categorias) {
//            if (repoCategoria.findByNombreIgnoreCase(categoria.getNombre()).isEmpty()) {
//                repoCategoria.save(categoria);
//            }
//        }
//    }
//
//    private void crearColeccionesDePrueba() {
//        Categoria caidaAeronave = repoCategoria.findByNombreIgnoreCase("Caída de aeronave").orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
//        Categoria precipitacionGranizo = repoCategoria.findByNombreIgnoreCase("Precipitación de granizo").orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
//        Categoria incidenteAviation = repoCategoria.findByNombreIgnoreCase("Incidente de aviación").orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
//        Categoria vientosFuertes = repoCategoria.findByNombreIgnoreCase("Vientos fuertes").orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
//
//        // Colección 1: Accidentes Aéreos Recientes
//        Coleccion coleccion1 = new Coleccion("Accidentes Aéreos", "Accidentes e incidentes aéreos ocurridos");
//        coleccion1.setTipoAlgoritmoConsenso(TipoAlgoritmoConsenso.MAYORIA_SIMPLE);
//        coleccion1.agregarFuente("FuenteDinamica::FD-1");
//        coleccion1 = repoColecciones.save(coleccion1);
//
//        CriterioPertenenciaFecha criterioFecha1 = new CriterioPertenenciaFecha(
//            "Período 2020-2024",
//            LocalDate.of(2020, 1, 1),
//            LocalDate.of(2024, 12, 31)
//        );
//        criterioFecha1.setColeccion(coleccion1);
//
//        CriterioPertenenciaCategoria criterioCat1 = new CriterioPertenenciaCategoria(
//            "Categoría Aeronave",
//            caidaAeronave.getNombre()
//        );
//        criterioCat1.setColeccion(coleccion1);
//
//        coleccion1.agregarCriterio(criterioFecha1);
//        coleccion1.agregarCriterio(criterioCat1);
//        repoColecciones.save(coleccion1);
//
//        // Colección 2: Desastres Naturales - Granizo
//        Coleccion coleccion2 = new Coleccion("Desastres Naturales - Granizo", "Eventos relacionados con tormentas de granizo");
//        coleccion2.setTipoAlgoritmoConsenso(TipoAlgoritmoConsenso.MULTIPLES_MENCIONES);
//        coleccion2.agregarFuente("FuenteDinamica::FD-1");
//        coleccion2 = repoColecciones.save(coleccion2);
//
//        CriterioPertenenciaCategoria criterioCat2 = new CriterioPertenenciaCategoria(
//            "Fenómeno Granizo",
//            precipitacionGranizo.getNombre()
//        );
//        criterioCat2.setColeccion(coleccion2);
//        coleccion2.agregarCriterio(criterioCat2);
//        repoColecciones.save(coleccion2);
//
//        // Colección 3: Incidentes Aéreos Históricos
//        Coleccion coleccion3 = new Coleccion("Incidentes Aéreos 2000-2010", "Incidentes en la primera década del siglo XXI");
//        coleccion3.setTipoAlgoritmoConsenso(TipoAlgoritmoConsenso.ABSOLUTA);
//        coleccion3.agregarFuente("FuenteDinamica::FD-1");
//        coleccion3 = repoColecciones.save(coleccion3);
//
//        CriterioPertenenciaFecha criterioFecha3 = new CriterioPertenenciaFecha(
//            "Década 2000-2010",
//            LocalDate.of(2000, 1, 1),
//            LocalDate.of(2010, 12, 31)
//        );
//        criterioFecha3.setColeccion(coleccion3);
//
//        CriterioPertenenciaCategoria criterioCat3a = new CriterioPertenenciaCategoria(
//            "Incidentes Aéreos",
//            incidenteAviation.getNombre()
//        );
//        criterioCat3a.setColeccion(coleccion3);
//
//        coleccion3.agregarCriterio(criterioFecha3);
//        coleccion3.agregarCriterio(criterioCat3a);
//        repoColecciones.save(coleccion3);
//
//        // ... (el resto de las colecciones se pueden seguir añadiendo si es necesario)
//
//        System.out.println("Seeder ejecutado: " + repoColecciones.count() + " colecciones creadas.");
//    }
}

