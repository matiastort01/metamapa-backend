package __tpa_mi_no_grupo_22.fuente_dinamica.models.entities.hecho.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.file.Path;
import java.nio.file.Paths;


@Converter(autoApply = true)
public class PathAtributteConverter implements AttributeConverter<Path, String> {
  @Override
  public String convertToDatabaseColumn(Path attribute) {
    return (attribute == null) ? null : attribute.toString();
  }

  @Override
  public Path convertToEntityAttribute(String dbData) {
    return (dbData == null) ? null : Paths.get(dbData);
  }
}
