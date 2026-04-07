package __tpa_mi_no_grupo_22.servicio_agregador.mappers;

import __tpa_mi_no_grupo_22.servicio_agregador.exceptions.ValidationBusinessException;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.input.CriterioPertenenciaInputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output.CriterioPertenenciaOutputDTO;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.ACriterioDePertenencia;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.TipoCriterio;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaCategoria;
import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.criterios.impl.CriterioPertenenciaFecha;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CriterioPertenenciaMapper {
  public CriterioPertenenciaOutputDTO toCriterioPertenenciaOutputDTO(ACriterioDePertenencia criterio) {
    Map<String, Object> parametros = new HashMap<>();

    if (criterio instanceof CriterioPertenenciaCategoria c) {
      parametros.put("categoria", c.getCategoria());

      return CriterioPertenenciaOutputDTO.builder()
          .nombreCriterio(c.getNombre())
          .tipoCriterio(TipoCriterio.CATEGORIA)
          .parametros(parametros)
          .build();

    } else if (criterio instanceof CriterioPertenenciaFecha c) {
      parametros.put("fechaInicio", c.getFechaInicio());
      parametros.put("fechaFin", c.getFechaFin());

      return CriterioPertenenciaOutputDTO.builder()
          .nombreCriterio(c.getNombre())
          .tipoCriterio(TipoCriterio.FECHA)
          .parametros(parametros)
          .build();

    } else {
      throw new IllegalArgumentException(
          "Tipo de criterio no soportado: " + criterio.getClass().getSimpleName()
      );
    }
  }

  public ACriterioDePertenencia toCriterioPertenencia(CriterioPertenenciaInputDTO criterioPertenenciaInputDTO) {
    if (criterioPertenenciaInputDTO == null) return null;

    Map<String, Object> parametros = criterioPertenenciaInputDTO.getParametros();
    if (parametros == null || parametros.isEmpty()) {
      throw new ValidationBusinessException(
          Map.of("criteriosDePertenencia", "Los parámetros son obligatorios.")
      );
    }

    switch (criterioPertenenciaInputDTO.getTipoCriterio()) {
      case FECHA -> {
        Object inicioRaw = parametros.get("fechaInicio");
        Object finRaw = parametros.get("fechaFin");

        if (inicioRaw == null || finRaw == null) {
          throw new ValidationBusinessException(
              Map.of("criterioFecha", "Los parámetros 'fechaInicio' y 'fechaFin' son obligatorios para el criterio FECHA.")
          );
        }

        try {
          LocalDate fechaInicio = LocalDate.parse(inicioRaw.toString());
          LocalDate fechaFin = LocalDate.parse(finRaw.toString());

          if (fechaInicio.isAfter(fechaFin)) {
            throw new ValidationBusinessException(
                Map.of("criterioFecha", "La fecha de inicio no puede ser posterior a la fecha fin.")
            );
          }

          return new CriterioPertenenciaFecha(criterioPertenenciaInputDTO.getNombreCriterio(), fechaInicio, fechaFin);
        } catch (DateTimeParseException e) {
          throw new ValidationBusinessException(
              Map.of("criterioFecha", "Formato de fecha inválido en criterio '" + criterioPertenenciaInputDTO.getNombreCriterio() + "'. Use 'yyyy-MM-dd'.")
          );
        }
      }
      case CATEGORIA -> {
        if (!parametros.containsKey("categoria") || parametros.get("categoria") == null) {
          throw new ValidationBusinessException(
              Map.of("criterioCategoria", "El parámetro 'categoria' es obligatorio para el criterio CATEGORIA.")
          );
        }
        return new CriterioPertenenciaCategoria(
            criterioPertenenciaInputDTO.getNombreCriterio(),
            parametros.get("categoria").toString().trim()
        );
      }
      default -> throw new ValidationBusinessException(
          Map.of("criteriosDePertenencia", "Tipo de criterio no soportado: " + criterioPertenenciaInputDTO.getTipoCriterio())
      );
    }
  }
}
