package __tpa_mi_no_grupo_22.servicio_agregador.models.entities.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String value = p.getText();

    if (value == null || value.isBlank()) {
      return null;
    }

    // Primero intentamos parsear como LocalDateTime
    try {
      return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    } catch (Exception e) {
      // Si falla, intentamos como LocalDate
      LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
      return date.atStartOfDay(); // agrega 00:00:00
    }
  }
}
