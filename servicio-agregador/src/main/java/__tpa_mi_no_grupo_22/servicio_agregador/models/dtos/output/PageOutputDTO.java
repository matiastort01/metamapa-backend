package __tpa_mi_no_grupo_22.servicio_agregador.models.dtos.output;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageOutputDTO<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
  public PageOutputDTO(Page<T> p) {
    this(
        p.getContent(),
        p.getNumber(),
        p.getSize(),
        p.getTotalElements(),
        p.getTotalPages()
    );
  }
}
