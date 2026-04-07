package metaMapa.utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import metaMapa.hechos.Categoria;
import metaMapa.hechos.HechoDeTexto;
import metaMapa.hechos.Origen;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * ImportadorCSV: lee un CSV de hechos usando el título como clave primaria.
 * Ahora ignora mayúsculas, minúsculas y tildes en los nombres de columna.
 */
public class ImportadorCSV {
  private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("d/M/yyyy");
  private static final Map<String, Categoria> CATEGORIA_CACHE = new LinkedHashMap<>();

  /**
   * Normaliza un string: elimina tildes y pasa a minúsculas.
   */
  private static String normalize(String s) {
    String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
    return temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase(Locale.ROOT);
  }

  /**
   * Importa hechos desde un archivo CSV.
   *
   * @param csvFile archivo CSV a procesar
   * @return lista de HechoDeTexto importados (sin duplicados por título)
   * @throws IOException en errores de lectura
   */
  public static List<HechoDeTexto> importar(Path csvFile) throws IOException {
    Map<String, HechoDeTexto> hechosPorTitulo = new LinkedHashMap<>();

    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setDelimiter(',')
        .setHeader()
        .setSkipHeaderRecord(false)
        .setIgnoreHeaderCase(true)
        .setTrim(true)
        .build();

    try (Reader reader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8);
         CSVParser parser = new CSVParser(reader, format)) {

      // Construir mapa de nombres de columna normalizados a nombres originales
      Map<String, String> normMap = new HashMap<>();
      for (String header : parser.getHeaderNames()) {
        normMap.put(normalize(header), header);
      }
      // Mapas de columnas requeridas
      Map<String, String> colMap = new HashMap<>();
      for (String col : new String[]{"titulo", "descripcion", "categoria", "latitud", "longitud", "fecha del hecho"}) {
        String key = normalize(col);
        if (!normMap.containsKey(key)) {
          throw new IllegalArgumentException("Columna no encontrada: " + col);
        }
        colMap.put(col, normMap.get(key));
      }

      for (CSVRecord rec : parser) {
        String titulo = rec.get(colMap.get("titulo"));
        String latStr = rec.get(colMap.get("latitud"));
        String lonStr = rec.get(colMap.get("longitud"));

        if (titulo == null || titulo.isBlank() || latStr.isBlank() || lonStr.isBlank()) {
          System.out.println("Registro omitido por título o coordenadas vacías, línea " + rec.getRecordNumber());
          continue;
        }

        LocalDate fecha;
        try {
          fecha = LocalDate.parse(rec.get(colMap.get("fecha del hecho")), FECHA_FMT);
        } catch (DateTimeParseException e) {
          System.out.println("Registro omitido por fecha inválida, línea " + rec.getRecordNumber());
          continue;
        }

        double lat, lon;
        try {
          lat = Double.parseDouble(latStr);
          lon = Double.parseDouble(lonStr);
        } catch (NumberFormatException e) {
          System.out.println("Registro omitido por coordenadas no numéricas, línea " + rec.getRecordNumber());
          continue;
        }

        String catStr = rec.get(colMap.get("categoria"));
        Categoria categoria = CATEGORIA_CACHE.computeIfAbsent(catStr, Categoria::new);

        HechoDeTexto hecho = HechoDeTexto.builder()
            .titulo(titulo)
            .descripcion(rec.get(colMap.get("descripcion")))
            .categoria(categoria)
            .ubicacion(new Ubicacion(lat, lon))
            .origen(Origen.Dataset)
            .fechaHecho(fecha)
            .build();

        // Actualiza o inserta según título
        hechosPorTitulo.put(titulo, hecho);
      }
    }

    return new ArrayList<>(hechosPorTitulo.values());
  }

  //  Main de prueba
  public static void main(String[] args) {
    Path csvPath = Path.of("src/test/resources/desastres_naturales_argentina.csv"); // uso el csv que mandaron aunque tiene espacios en la propiedad "fecha del hecho"

    try {
      List<HechoDeTexto> hechos = importar(csvPath);
      hechos.forEach(h -> {
        System.out.println();
        System.out.printf(
            "Título: %s, Descripción: %s, Categoria: %s, Latitud: %.4f, Longitud: %.4f, Fecha: %s%n",
            h.getTitulo(),
            h.getDescripcion(),
            h.getCategoria().getNombre(),
            h.getUbicacion().getLatitud(),
            h.getUbicacion().getLongitud(),
            h.getFechaHecho().format(FECHA_FMT)
        );
      });
      System.out.println("Total importados: " + hechos.size());
    } catch (IOException e) {
      System.err.println("Error al importar CSV: " + e.getMessage());
    }
  }
}
