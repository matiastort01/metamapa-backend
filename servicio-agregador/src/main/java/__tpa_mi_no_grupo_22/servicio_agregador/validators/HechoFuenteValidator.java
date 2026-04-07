package __tpa_mi_no_grupo_22.servicio_agregador.validators;

import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.HechoFuenteInputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.SynchronousSink;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Este validator sirve para validar los datos entrantes de las fuentes. No se puede utilzar los @Valid dado que si un hecho falla alguna validacion, se cortaría todo el pedido. Con esto simplemente descarto el q no cumpla con las validaciones.
public class HechoFuenteValidator {
  private static final Logger log = LoggerFactory.getLogger(HechoFuenteValidator.class);

  // Constantes para límites (buena práctica para no tener números mágicos)
  private static final int MAX_TITULO = 100;
  private static final int MAX_DESCRIPCION = 500;
  private static final int MAX_CATEGORIA = 100;
  private static final BigDecimal LAT_MIN = new BigDecimal("-90.0");
  private static final BigDecimal LAT_MAX = new BigDecimal("90.0");
  private static final BigDecimal LON_MIN = new BigDecimal("-180.0");
  private static final BigDecimal LON_MAX = new BigDecimal("180.0");

  private HechoFuenteValidator() {
    // Constructor privado para evitar instanciación
  }

  public static void validarHecho(HechoFuenteInputDTO dto, SynchronousSink<HechoFuenteInputDTO> sink) {
    // 0. Validación estructural básica
    if (dto == null) return;

    // Usamos un identificador para los logs (si fuenteId viene nulo, ponemos "DESCONOCIDO")
    String logId = (dto.getFuenteId() != null && !dto.getFuenteId().isBlank())
        ? dto.getFuenteId()
        : "DESCONOCIDO";

    // 1. Validar Fuente ID (@NotBlank)
    if (dto.getFuenteId() == null || dto.getFuenteId().isBlank()) {
      log.warn("Hecho descartado: La identificación de la fuente es obligatoria.");
      return;
    }

    // 2. Validar Título (@NotBlank, @Size max=100)
    if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
      log.warn("Hecho descartado [FuenteID: {}]: El título es obligatorio.", logId);
      return;
    }
    if (dto.getTitulo().length() > MAX_TITULO) {
      log.warn("Hecho descartado [FuenteID: {}]: Título demasiado largo ({} chars). Máximo permitido: {}.",
          logId, dto.getTitulo().length(), MAX_TITULO);
      return;
    }

    // 3. Validar Descripción (@NotBlank, @Size max=500)
    if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
      log.warn("Hecho descartado [FuenteID: {}]: La descripción es obligatoria.", logId);
      return;
    }
    if (dto.getDescripcion().length() > MAX_DESCRIPCION) {
      log.warn("Hecho descartado [FuenteID: {}]: Descripción demasiado larga ({} chars). Máximo permitido: {}.",
          logId, dto.getDescripcion().length(), MAX_DESCRIPCION);
      return;
    }

    // 4. Validar Categoría (@NotBlank, @Size max=100)
    if (dto.getCategoria() == null || dto.getCategoria().isBlank()) {
      log.warn("Hecho descartado [FuenteID: {}]: La categoría es obligatoria.", logId);
      return;
    }
    if (dto.getCategoria().length() > MAX_CATEGORIA) {
      log.warn("Hecho descartado [FuenteID: {}]: Categoría demasiado larga ({} chars). Máximo permitido: {}.",
          logId, dto.getCategoria().length(), MAX_CATEGORIA);
      return;
    }

    // 5. Validar Ubicación: Latitud (@NotNull, Min -90, Max 90)
    if (dto.getLatitud() == null) {
      log.warn("Hecho descartado [FuenteID: {}]: La latitud es obligatoria.", logId);
      return;
    }
    if (dto.getLatitud().compareTo(LAT_MIN) < 0 || dto.getLatitud().compareTo(LAT_MAX) > 0) {
      log.warn("Hecho descartado [FuenteID: {}]: Latitud inválida ({}). Debe estar entre -90 y 90.",
          logId, dto.getLatitud());
      return;
    }

    // 6. Validar Ubicación: Longitud (@NotNull, Min -180, Max 180)
    if (dto.getLongitud() == null) {
      log.warn("Hecho descartado [FuenteID: {}]: La longitud es obligatoria.", logId);
      return;
    }
    if (dto.getLongitud().compareTo(LON_MIN) < 0 || dto.getLongitud().compareTo(LON_MAX) > 0) {
      log.warn("Hecho descartado [FuenteID: {}]: Longitud inválida ({}). Debe estar entre -180 y 180.",
          logId, dto.getLongitud());
      return;
    }

    // 7. Validar Fecha (@NotNull, @PastOrPresent)
    if (dto.getFecha() == null) {
      log.warn("Hecho descartado [FuenteID: {}]: La fecha es obligatoria.", logId);
      return;
    }
    // Validamos que no sea futura (con un pequeño margen de tolerancia si quisieras, aquí es estricto)
    if (dto.getFecha().isAfter(LocalDateTime.now())) {
      log.warn("Hecho descartado [FuenteID: {}]: La fecha ({}) no puede ser futura.", logId, dto.getFecha());
      return;
    }

    // Si pasa todas las validaciones, lo aprobamos
    sink.next(dto);
  }
}
