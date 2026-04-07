package __tpa_mi_no_grupo_22.fuente_estatica.services.ImportadorCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.Hecho;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Categoria;
import __tpa_mi_no_grupo_22.fuente_estatica.models.entities.hechoInfo.Ubicacion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class ImportadorCSV {
  private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("d/M/yyyy"); //comentario

  private static String normalize(String s) {
    String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
    return temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase(Locale.ROOT);
  }

  public List<Hecho> importar(Path csvFile) throws IOException {
    List<Hecho> listaHechos = new ArrayList<>();

    // Usamos un caché local SOLO para este archivo, para no duplicar categorías dentro del mismo CSV
    Map<String, Categoria> categoriasDelArchivo = new HashMap<>();

    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setDelimiter(',')
        .setHeader()
        .setSkipHeaderRecord(false)
        .setIgnoreHeaderCase(true)
        .setTrim(true)
        .build();

    try (Reader reader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8);
         CSVParser parser = new CSVParser(reader, format)) {

      Map<String, String> normMap = new HashMap<>();
      for (String header : parser.getHeaderNames()) {
        normMap.put(normalize(header), header);
      }

      Map<String, String> colMap = new HashMap<>();
      for (String col : new String[]{"titulo", "descripcion", "categoria", "latitud", "longitud", "fecha_del_hecho", "multimedia"}) {
        String key = normalize(col);
        if (normMap.containsKey(key)) {
          colMap.put(col, normMap.get(key));
        }
      }

      if (!colMap.containsKey("titulo")) {
        // Si no hay titulo, devolvemos lista vacía o error, pero no rompemos todo
        System.out.println("CSV sin columna titulo, omitiendo.");
        return listaHechos;
      }

      for (CSVRecord rec : parser) {
        String titulo = rec.get(colMap.get("titulo"));
        if (titulo == null || titulo.isBlank()) continue;

        // --- Lógica de Fechas ---
        LocalDate fecha = null;
        if (colMap.containsKey("fecha_del_hecho")) {
          String fechaStr = rec.get(colMap.get("fecha_del_hecho"));
          if (fechaStr != null && !fechaStr.isBlank()) {
            try { fecha = LocalDate.parse(fechaStr, FECHA_FMT); } catch (DateTimeParseException e) {}
          }
        }
        // ------------------------

        // --- Lógica de Ubicación ---
        Double lat = null, lon = null;
        try {
          String latStr = colMap.containsKey("latitud") ? rec.get(colMap.get("latitud")) : null;
          String lonStr = colMap.containsKey("longitud") ? rec.get(colMap.get("longitud")) : null;
          if (latStr != null && !latStr.isBlank()) lat = Double.parseDouble(latStr);
          if (lonStr != null && !lonStr.isBlank()) lon = Double.parseDouble(lonStr);
        } catch (NumberFormatException e) {}
        // ---------------------------

        // --- Lógica de Categoría ---
        String catStr = colMap.containsKey("categoria") ? rec.get(colMap.get("categoria")) : "Sin Categoria";
        if (catStr == null || catStr.isBlank()) catStr = "Sin Categoria";

        // Reutilizamos instancia dentro del mismo archivo, pero es nueva para la DB
        Categoria categoria = categoriasDelArchivo.computeIfAbsent(catStr, Categoria::new);

        Hecho hecho = Hecho.builder()
            .titulo(titulo)
            .descripcion(colMap.containsKey("descripcion") ? rec.get(colMap.get("descripcion")) : "")
            .categoria(categoria)
            .ubicacion(Ubicacion.builder().latitud(lat).longitud(lon).build())
            .fechaHecho(fecha != null ? fecha : LocalDate.now()) // Fecha default si falla
            .archivoCSV(csvFile)
            .build();

        listaHechos.add(hecho);
      }
    }
    return listaHechos;
  }
}