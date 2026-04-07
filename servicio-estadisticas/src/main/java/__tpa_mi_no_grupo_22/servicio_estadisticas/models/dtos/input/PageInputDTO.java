package __tpa_mi_no_grupo_22.servicio_estadisticas.models.dtos.input;

import java.util.List;

public record PageInputDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) { }