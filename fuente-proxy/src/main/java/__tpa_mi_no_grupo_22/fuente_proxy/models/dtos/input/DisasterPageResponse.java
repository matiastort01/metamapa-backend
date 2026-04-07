package __tpa_mi_no_grupo_22.fuente_proxy.models.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class DisasterPageResponse {
  @JsonProperty("current_page")
  private Integer currentPage;

  @JsonProperty("last_page")
  private Integer lastPage;

  @JsonProperty("next_page_url")
  private String nextPageUrl;

  @JsonProperty("prev_page_url")
  private String prevPageUrl;
  private List<DisasterInputDTO> data;

}