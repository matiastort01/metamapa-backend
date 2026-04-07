package metaMapa.fuentes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import metaMapa.hechos.HechoDeTexto;
import metaMapa.utils.ImportadorCSV;

public class FuenteEstatica {
  private String identificador;

  public List<HechoDeTexto> obtenerHechos(Path path) throws IOException {
    // al ser un método estático, se puede llamar directamente sin instanciar al importador
    return ImportadorCSV.importar(path);
  }
}
